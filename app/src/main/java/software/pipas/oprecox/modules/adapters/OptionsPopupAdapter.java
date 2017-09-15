package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

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
            v = vi.inflate(R.layout.popup_list_item, null);
        }

        String option = getItem(position);

        if (option != null)
        {
            TextView textTooltip = (TextView) v.findViewById(R.id.textTooltip);

            if (textTooltip != null)
            {
                textTooltip.setText(option);
                CustomFontHelper.setCustomFont(textTooltip, "font/antipastopro-demibold.otf", mContext);
            }
        }

        return v;
    }
}
