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

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;

public class AdOverviewAdapter extends ArrayAdapter<Ad>
{
    Context mContext;
    ContentResolver mContentResolver;

    public AdOverviewAdapter(ArrayList<Ad> data, Context context, ContentResolver contentResolver)
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

        Ad ad = getItem(position);

        if (ad != null)
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
                title.setText(ad.getTitle());
                CustomFontHelper.setCustomFont(title, "font/antipastopro-demibold.otf", mContext);
            }

            if (scoreGained != null)
            {
                if(ad.getScoreGained() != 0)
                {
                    scoreGained.setTextColor(ContextCompat.getColor(mContext, R.color.correctGreen));
                    scoreGained.setText(String.format("+%d", ad.getScoreGained()));
                }
                else
                {
                    scoreGained.setTextColor(ContextCompat.getColor(mContext, R.color.letterGrey));
                    scoreGained.setText(String.format("%d", ad.getScoreGained()));
                }

                CustomFontHelper.setCustomFont(scoreGained, "font/Comfortaa_Thin.ttf", mContext);
            }

            if (image != null)
            {
                image.setImageBitmap(ad.getImages().get(0));
            }
        }

        return v;
    }
}
