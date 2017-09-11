package software.pipas.oprecox.util;

import android.util.DisplayMetrics;

public abstract class Settings
{
    private static DisplayMetrics deviceDisplayMetrics;

    public static DisplayMetrics getDeviceDisplayMetrics()
    {
        return deviceDisplayMetrics;
    }

    public static void setDeviceDisplayMetrics(DisplayMetrics deviceDisplayMetrics)
    {
        Settings.deviceDisplayMetrics = deviceDisplayMetrics;
    }
}
