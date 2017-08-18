package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

import software.pipas.oprecox.util.Util;

public class Player
{
    private String name;
    private String displayName;
    private String id;
    private Uri playerImage;
    private long timeAnnounced;

    public Player(String name, String displayName, String id, Uri playerImage, long timeAnnounced)
    {
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.playerImage = playerImage;
        this.timeAnnounced = timeAnnounced;
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

    public void updatePlayerAnnouncedTime(long newTimeAnnounced) {this.timeAnnounced = newTimeAnnounced;}

    public void updatePlayerImage(Uri image) { this.playerImage = image;}

    @Override
    public boolean equals(Object player)
    {
        if(!(player instanceof Player)) return false;
        String playerId = ((Player) player).getPlayerID();
        return playerId.equals(this.id);
    }
}
