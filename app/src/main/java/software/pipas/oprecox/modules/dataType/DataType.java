package software.pipas.oprecox.modules.dataType;

import android.net.Uri;

/**
 * Created by nuno_ on 19-Aug-17.
 */

public abstract class DataType
{
    protected String playerID;
    protected Uri playerImage;

    public String getPlayerID()
    {
        return playerID;
    }

    public Uri getPlayerImage()
    {
        return playerImage;
    }

    public void updateImage(Uri playerImage)
    {
        this.playerImage = playerImage;
    }
}
