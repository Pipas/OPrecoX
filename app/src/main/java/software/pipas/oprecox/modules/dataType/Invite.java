package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

public class Invite
{
    private String roomName;
    private String roomHost;
    private Uri roomPicture;

    public Invite(String roomName, String roomHost, Uri roomPicture)
    {
        this.roomName = roomName;
        this.roomHost = roomHost;
        this.roomPicture = roomPicture;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRoomHost()
    {
        return roomHost;
    }

    public Uri getRoomPicture()
    {
        return roomPicture;
    }
}
