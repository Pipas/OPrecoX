package software.pipas.oprecox.modules.dataType;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.InetAddress;

import software.pipas.oprecox.util.Util;

public class Player extends DataType implements Parcelable
{
    private String name;
    private String displayName;
    private Long timeAnnounced;
    private Integer invitePort;
    private InetAddress address;

    public Player(String name, String displayName, String id, Uri playerImage, Integer invitePort, InetAddress address,Long timeAnnounced)
    {
        this.name = name;
        this.displayName = displayName;
        this.playerID = id;
        this.playerImage = playerImage;
        this.timeAnnounced = timeAnnounced;
        this.invitePort = invitePort;
        this.address = address;

    }

    public Player(String name, String displayName, String id, Uri playerImage)
    {
        this.name = name;
        this.displayName = displayName;
        this.playerID = id;
        this.playerImage = playerImage;
    }

    public Player(String playerID)
    {
        this.playerID = playerID;
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

    public void updatePlayerDisplayName(String name) {this.displayName = Util.substituteSpace(name);}

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

    /**
     * Parcelable stuff
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(displayName);
        dest.writeString(playerID);
        dest.writeParcelable(playerImage, flags);
        dest.writeLong(timeAnnounced);
        dest.writeInt(invitePort);
        dest.writeSerializable(address);
    }

    protected Player(Parcel in) {
        name = in.readString();
        displayName = in.readString();
        playerID = in.readString();
        playerImage = in.readParcelable(Uri.class.getClassLoader());
        timeAnnounced = in.readLong();
        invitePort = in.readInt();
        address = (InetAddress) in.readSerializable();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
