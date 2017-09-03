package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

import java.net.InetAddress;

import software.pipas.oprecox.util.Util;

public class Player extends DataType
{
    private String name;
    private String displayName;
    private long timeAnnounced;
    private int invitePort;
    private InetAddress address;

    public Player(String name, String displayName, String id, Uri playerImage, int invitePort, InetAddress address,long timeAnnounced)
    {
        this.name = name;
        this.displayName = displayName;
        this.playerID = id;
        this.playerImage = playerImage;
        this.timeAnnounced = timeAnnounced;
        this.invitePort = invitePort;
        this.address = address;

    }

    public String getName()
    {
        return this.name;
    }

    public String getRealName() {return Util.substituteUnder(this.name);}

    public String getDisplayName() {return this.displayName;}

    public long getTimeAnnounced(){ return  this.timeAnnounced;}

    public int getInvitePort() {return this.invitePort;}

    public InetAddress getAddress() {return this.address;}

    public void updatePlayerAnnouncedTime(long newTimeAnnounced) {this.timeAnnounced = newTimeAnnounced;}

    public void updatePlayerInvitePort(int invitePort) {this.invitePort = invitePort;}

    public void updatePlayerAddress(InetAddress address) {this.address = address;}


    @Override
    public void updateImage(Uri image) {this.playerImage = image;}

    @Override
    public String getPlayerID()
    {
        return this.playerID;
    }

    @Override
    public Uri getPlayerImage()
    {
        return this.playerImage;
    }

    @Override
    public boolean equals(Object player)
    {
        if(!(player instanceof Player)) return false;
        String playerId = ((Player) player).getPlayerID();
        return playerId.equals(this.playerID);
    }

    @Override
    public String toString()
    {
        String str = new String(
                "DataType: Player" + "\n" +
                "PlayerAnnouncedName | Real: " +  this.name + " " + this.getRealName() + "\n" +
                "PlayerAnnouncedDisplayName: " + this.displayName + "\n" +
                "PlayerAnnouncedID: " + this.playerID + "\n" +
                "PlayerAnnouncedImage: " + this.getPlayerImage().toString() + "\n" +
                "TimeAnnounced: " + this.timeAnnounced + "\n" +
                "InvitePort: " + this.invitePort + "\n" +
                "PlayerAddress: " + this.address);
        return str;
    }
}
