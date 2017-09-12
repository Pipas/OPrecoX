package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import software.pipas.oprecox.R;

public class OptionsPopupAdapter extends ArrayAdapter<String>
{
    Context mContext;
    ContentResolver mContentResolver;

    public OptionsPopupAdapter(ArrayList<String> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.popup_list_item, data);
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

        String option = getItem(position);

        /*if (ad != null)
        {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView description = (TextView) v.findViewById(R.id.description);

            CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.image);

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
        }*/

        return v;
    }
}
