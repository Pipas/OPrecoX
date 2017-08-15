package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

public class Player
{
    private String name;
    private String id;
    private Uri playerImage;
    private long timeAnnounced;

    public Player(String name, String id, Uri playerImage, long timeAnnounced)
    {
        this.name = name;
        this.id = id;
        this.playerImage = playerImage;
        this.timeAnnounced = timeAnnounced;
    }

    public String getPlayerName()
    {
        return this.name;
    }

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

    @Override
    public boolean equals(Object player)
    {
        if(!(player instanceof Player)) return false;
        String playerId = ((Player) player).getPlayerID();
        return playerId.equals(this.id);
    }
}
