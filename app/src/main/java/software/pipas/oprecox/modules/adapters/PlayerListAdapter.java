package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
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

public class PlayerListAdapter extends ArrayAdapter<Player>
{
    Context mContext;
    ContentResolver mContentResolver;

    public PlayerListAdapter(ArrayList<Player> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.player_item_layout, data);
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
            v = vi.inflate(R.layout.player_item_layout, null);
        }

        Player player = getItem(position);

        if (player != null)
        {
            TextView playerName = (TextView) v.findViewById(R.id.playerPlayerName);
            ImageView playerImage = (ImageView) v.findViewById(R.id.playerPlayerImage);

            if (playerName != null)
            {
                playerName.setText(player.getDisplayName());
                CustomFontHelper.setCustomFont(playerName, "font/antipastopro-demibold.otf", mContext);
            }

            if (playerImage != null)
            {
                ImageManager manager = ImageManager.create(mContext);
                manager.loadImage(playerImage, player.getPlayerImage());
            }
        }

        return v;
    }


}
