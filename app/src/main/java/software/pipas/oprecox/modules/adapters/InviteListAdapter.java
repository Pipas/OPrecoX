package software.pipas.oprecox.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;

import java.util.ArrayList;
import java.util.List;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.dataType.Invite;

public class InviteListAdapter extends ArrayAdapter<Invite> implements View.OnClickListener
{
    Context mContext;

    public InviteListAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
        mContext = context;
    }

    public InviteListAdapter(Context context, int resource, List<Invite> items)
    {
        super(context, resource, items);
        mContext = context;
    }

    @Override
    public void onClick(View v)
    {
        /*int position=(Integer) v.getTag();
        Object object = getItem(position);
        Invite invite = (Invite)object;*/


    }

    private int lastPosition = -1;

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Invite invite = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new View();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(invite.getName());
        viewHolder.txtType.setText(invite.getType());
        viewHolder.txtVersion.setText(invite.getVersion_number());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }*/

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

        Invite invite = getItem(position);

        if (invite != null)
        {
            TextView inviteHostName = (TextView) v.findViewById(R.id.inviteHostName);
            TextView inviteRoomName = (TextView) v.findViewById(R.id.inviteRoomName);
            ImageView inviteImage = (ImageView) v.findViewById(R.id.inviteImage);

            if (inviteHostName != null) {
                inviteHostName.setText(invite.getRoomHost());
            }

            if (inviteRoomName != null) {
                inviteRoomName.setText(invite.getRoomName());
            }

            if (inviteImage != null) {
                ImageManager manager = ImageManager.create(mContext);
                manager.loadImage(inviteImage, invite.getRoomPicture());
            }
        }

        return v;
    }
}
