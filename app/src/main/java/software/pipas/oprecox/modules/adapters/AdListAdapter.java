package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
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
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.util.Util;

public class AdListAdapter extends ArrayAdapter<AdPreview>
{
    Context mContext;
    ContentResolver mContentResolver;

    public AdListAdapter(ArrayList<AdPreview> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.saved_ad_layout, data);
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
            v = vi.inflate(R.layout.saved_ad_layout, null);
        }

        AdPreview ad = getItem(position);

        if (ad != null)
        {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView description = (TextView) v.findViewById(R.id.description);
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

            if (description != null)
            {
                description.setText(ad.getDescription());
                CustomFontHelper.setCustomFont(description, "font/Comfortaa_Regular.ttf", mContext);
            }

            if (image != null)
            {
                image.setImageBitmap(Util.biteArrayToBitmap(ad.getThumbnail()));
            }
        }

        return v;
    }
}
