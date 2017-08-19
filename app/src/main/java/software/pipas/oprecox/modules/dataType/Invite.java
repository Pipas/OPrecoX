package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

import java.net.InetAddress;

import software.pipas.oprecox.util.Util;

public class Invite extends DataType
{
    private String roomName;
    private String displayName;
    private String name;
    private int roomPort;
    private InetAddress address;
    private long timeReceived;

    public Invite(String roomName, String displayName, String name, String playerID, String roomPort, InetAddress address, long timeReceived, Uri image)
    {
        this.roomName = roomName;
        this.displayName = displayName;
        this.name = name;
        this.playerID = playerID;
        this.roomPort = Integer.parseInt(roomPort);
        this.address = address;
        this.timeReceived = timeReceived;
        this.playerImage = image;

    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRealRoomName() {return Util.substituteUnder(roomName);}

    public String getDisplayName() {return displayName;}

    public String getName() {return name;}

    public String getRealName() {return Util.substituteUnder(name);}

    public int getRoomPort() {return roomPort;}

    public long getTimeReceived() {return timeReceived;}

    public InetAddress getAddress() {return address;}

    public void updateRoomName(String roomName) {this.roomName = roomName;}

    public void updateRoomPort(int roomPort) {this.roomPort = roomPort;}

    public void updateAddress(InetAddress address) {this.address = address;}

    public void updateTimeReceived(long timeReceived) {this.timeReceived = timeReceived;}


    @Override
    public Uri getPlayerImage() {return this.playerImage;}

    @Override
    public void updateImage(Uri image) {this.playerImage = image;}

    @Override
    public String getPlayerID() {return playerID;}

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
        String str = new String(
                "DataType: Invite" + "\n" +
                "RoomName: " + this.roomName + " " + this.getRealRoomName() +"\n" +
                "PlayerHostDisplayName: " + this.displayName + "\n" +
                "PlayerHostName | Real: " +  this.name + " " + this.getRealName() + "\n" +
                "PlayerHostID: " + this.playerID + "\n" +
                "RoomImage/PlayerHostImage: " + this.getPlayerImage().toString() + "\n" +
                "RoomPort: " + this.roomPort + "\n" +
                "RoomAddress: " + this.address + "\n" +
                "TimeReceived: " + this.timeReceived);
        return str;
    }
}
