package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

import java.net.InetAddress;

import software.pipas.oprecox.util.Util;

public class Invite extends DataType
{
    private String roomName;
    private String displayName;
    private String name;
    private String playerID;
    private int roomPort;
    private InetAddress address;
    private long timeReceived;
    private Uri image;

    public Invite(String roomName, String displayName, String name, String playerID, String roomPort, InetAddress address, long timeReceived, Uri image)
    {
        this.roomName = roomName;
        this.displayName = displayName;
        this.name = name;
        this.playerID = playerID;
        this.roomPort = Integer.parseInt(roomPort);
        this.address = address;
        this.timeReceived = timeReceived;
        this.image = image;

    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRealRoomName() {return Util.substituteUnder(roomName);}

    public String getDisplayName() {return displayName;}

    public String getName() {return name;}

    public String getRealName() {return Util.substituteUnder(name);}

    public String getPlayerID() {return playerID;}

    public int getRoomPort() {return roomPort;}

    public long getTimeReceived() {return timeReceived;}

    public Uri getImage() {return image;}

    public InetAddress getAddress() {return address;}

    public void updateRoomName(String roomName) {this.roomName = roomName;}

    public void updateRoomPort(String roomPort) {this.roomPort = Integer.parseInt(roomPort);}

    public void updateAddress(InetAddress address) {this.address = address;}

    public void updateTimeReceived(long timeReceived) {this.timeReceived = timeReceived;}

    public void setImage(Uri image) {this.image = image;}

    @Override
    public boolean equals(Object invite)
    {
        if(!(invite instanceof Invite)) return false;
        String playerId = ((Invite) invite).getPlayerID();
        return playerId.equals(this.playerID);
    }

    @Override
    public String toString()
    {
        return this.roomName + " " + this.displayName + " " + this.name + " " + this.playerID + " " + this.roomPort + " " + this.address + " " + this.timeReceived;
    }
}
