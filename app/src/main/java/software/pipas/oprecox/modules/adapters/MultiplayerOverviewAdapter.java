package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.modules.dataType.ScoredPlayer;
import software.pipas.oprecox.util.Settings;

public class MultiplayerOverviewAdapter extends ArrayAdapter<ScoredPlayer>
{
    Context mContext;
    ContentResolver mContentResolver;

    public MultiplayerOverviewAdapter(ArrayList<ScoredPlayer> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.ad_overview_layout, data);
        this.mContext = context;
        this.mContentResolver = contentResolver;
    }

    public void remove(int position)
    {
        super.remove(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.ad_overview_layout, null);
        }
        ScoredPlayer scoredPlayer = getItem(position);
        Player player = Settings.getSharedPlayerDB().get(Settings.getSharedPlayerDB().indexOf(scoredPlayer.getPlayer()));

        if (player != null)
        {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView scoreGained = (TextView) v.findViewById(R.id.scoreGained);
            ImageView image = (ImageView) v.findViewById(R.id.image);
            CardView cardViewClickable = (CardView) v.findViewById(R.id.cardViewClickable);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            {
                cardViewClickable.setRadius(0);
            }

            if (title != null)
            {
                title.setText(player.getDisplayName());
                CustomFontHelper.setCustomFont(title, "font/antipastopro-demibold.otf", mContext);
            }

            if (scoreGained != null)
            {
                if(scoredPlayer.getScore() != 0)
                {
                    scoreGained.setTextColor(ContextCompat.getColor(mContext, R.color.correctGreen));
                    scoreGained.setText(String.format("%d", scoredPlayer.getScore()));
                }
                else
                {
                    scoreGained.setTextColor(ContextCompat.getColor(mContext, R.color.letterGrey));
                    scoreGained.setText(String.format("%d", scoredPlayer.getScore()));
                }

                CustomFontHelper.setCustomFont(scoreGained, "font/Comfortaa_Thin.ttf", mContext);
            }

            if (image != null)
            {
                ImageManager manager = ImageManager.create(mContext);
                manager.loadImage(image, player.getPlayerImage());
            }
        }

        return v;
    }
}
