package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.util.Util;

public class AddListAdapter extends ArrayAdapter<AdPreview>
{
    Context mContext;
    ContentResolver mContentResolver;

    public AddListAdapter(ArrayList<AdPreview> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.invite_item_layout, data);
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
            v = vi.inflate(R.layout.invite_item_layout, null);
        }

        AdPreview add = getItem(position);

        if (add != null)
        {
            TextView inviteHostName = (TextView) v.findViewById(R.id.inviteHostName);
            TextView inviteRoomName = (TextView) v.findViewById(R.id.inviteRoomName);
            ImageView inviteImage = (ImageView) v.findViewById(R.id.inviteImage);

            if (inviteHostName != null)
            {
                inviteHostName.setText(add.getDescription());
            }

            if (inviteRoomName != null)
            {
                inviteRoomName.setText(add.getTitle());
            }

            if (inviteImage != null)
            {
                inviteImage.setImageBitmap(Util.biteArrayToBitmap(add.getThumbnail()));
            }
        }

        return v;
    }
}
