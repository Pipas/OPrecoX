package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

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

            CircularImageView circleImageView = (CircularImageView) v.findViewById(R.id.image);

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

            if (circleImageView != null)
            {
                circleImageView.setImageBitmap(Util.biteArrayToBitmap(ad.getThumbnail()));
            }
        }

        return v;
    }
}
