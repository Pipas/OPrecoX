package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

import java.net.InetAddress;

import software.pipas.oprecox.util.Util;

public class Player extends DataType
{
    private String name;
    private String displayName;
    private String id;
    private Uri playerImage;
    private long timeAnnounced;
    private int invitePort;
    private InetAddress address;

    public Player(String name, String displayName, String id, Uri playerImage, String invitePort, InetAddress address,long timeAnnounced)
    {
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.playerImage = playerImage;
        this.timeAnnounced = timeAnnounced;
        this.invitePort = Integer.parseInt(invitePort);
        this.address = address;

    }

    public String getName()
    {
        return this.name;
    }

    public String getRealName() {return Util.substituteUnder(this.name);}

    public String getDisplayName() {return this.displayName;}

    public String getPlayerID()
    {
        return this.id;
    }

    public Uri getPlayerImage()
    {
        return this.playerImage;
    }

    public long getTimeAnnounced(){ return  this.timeAnnounced;}

    public int getInvitePort() {return this.invitePort;}

    public InetAddress getAddress() {return this.address;}

    public void updatePlayerAnnouncedTime(long newTimeAnnounced) {this.timeAnnounced = newTimeAnnounced;}

    public void updatePlayerImage(Uri image) { this.playerImage = image;}

    public void updatePlayerInvitePort(int invitePort) {this.invitePort = invitePort;}

    public void updatePlayerAddress(InetAddress address) {this.address = address;}

    @Override
    public boolean equals(Object player)
    {
        if(!(player instanceof Player)) return false;
        String playerId = ((Player) player).getPlayerID();
        return playerId.equals(this.id);
    }

    @Override
    public String toString()
    {
        return this.name + " " + this.displayName + " " + this.id + " " + this.timeAnnounced + " " + this.invitePort + " " + this.address;
    }
}
