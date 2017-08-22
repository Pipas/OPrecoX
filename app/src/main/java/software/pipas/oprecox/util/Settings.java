package software.pipas.oprecox.util;

import android.util.DisplayMetrics;

public abstract class Settings
{
    private static boolean locked;
    private static DisplayMetrics deviceDisplayMetrics;

    public static void setLocked(boolean locked)
    {
        Settings.locked = locked;
    }

    public static boolean isLocked()
    {
        return locked;
    }

    public static DisplayMetrics getDeviceDisplayMetrics()
    {
        return deviceDisplayMetrics;
    }

    public static void setDeviceDisplayMetrics(DisplayMetrics deviceDisplayMetrics)
    {
        Settings.deviceDisplayMetrics = deviceDisplayMetrics;
    }
}
