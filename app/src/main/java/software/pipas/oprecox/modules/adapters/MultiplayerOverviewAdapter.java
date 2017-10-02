package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;

import java.util.HashMap;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.util.Settings;

public class MultiplayerOverviewAdapter extends BaseAdapter
{
    Context mContext;
    ContentResolver mContentResolver;

    private HashMap<Player, Integer> data = new HashMap<Player, Integer>();
    private Player[] keys;

    public MultiplayerOverviewAdapter(HashMap<Player, Integer> data, Context context, ContentResolver contentResolver)
    {
        this.data = data;
        this.keys = data.keySet().toArray(new Player[data.size()]);
        this.mContext = context;
        this.mContentResolver = contentResolver;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(keys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.ad_overview_layout, null);
        }

        Player player = keys[position];
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

            int playerIndex = Settings.getSharedPlayerDB().indexOf(player);


            if (title != null)
            {
                title.setText(Settings.getSharedPlayerDB().get(playerIndex).getDisplayName());
                CustomFontHelper.setCustomFont(title, "font/antipastopro-demibold.otf", mContext);
            }

            if (scoreGained != null)
            {
                if(data.get(player) != 0)
                    scoreGained.setTextColor(ContextCompat.getColor(mContext, R.color.correctGreen));
                else
                    scoreGained.setTextColor(ContextCompat.getColor(mContext, R.color.letterGrey));

                scoreGained.setText(String.format("%d", data.get(player)));
                CustomFontHelper.setCustomFont(scoreGained, "font/Comfortaa_Thin.ttf", mContext);
            }

            if (image != null)
            {
                ImageManager manager = ImageManager.create(mContext);
                manager.loadImage(image, Settings.getSharedPlayerDB().get(playerIndex).getPlayerImage());
            }
        }


        return v;
    }
}
