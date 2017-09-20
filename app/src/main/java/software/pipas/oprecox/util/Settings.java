package software.pipas.oprecox.util;

import android.util.DisplayMetrics;

public abstract class Settings
{
    private static DisplayMetrics deviceDisplayMetrics;
    private static int gameSize;

    public static DisplayMetrics getDeviceDisplayMetrics()
    {
        return deviceDisplayMetrics;
    }

    public static void setDeviceDisplayMetrics(DisplayMetrics deviceDisplayMetrics)
    {
        Settings.deviceDisplayMetrics = deviceDisplayMetrics;
    }

    public static int getGameSize()
    {
        return gameSize;
    }

    public static void setGameSize(int gS)
    {
        gameSize = gS;
    }
}
