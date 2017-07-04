package software.pipas.oprecox.util;

import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public abstract class Settings
{
    private static boolean locked;
    private static DisplayMetrics deviceDisplayMetrics;
    private static int newSavedAds;

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

    public static int getNewSavedAds()
    {
        return newSavedAds;
    }

    public static void setNewSavedAds(int newSavedAds)
    {
        Settings.newSavedAds = newSavedAds;
    }

    public static void incrementNewSavedAds(SharedPreferences.Editor editor)
    {
        Settings.newSavedAds++;
        editor.putInt("newSavedAds", newSavedAds);
        editor.apply();
    }

    public static void resetNewSavedAds(SharedPreferences.Editor editor)
    {
        Settings.newSavedAds = 0;
        editor.putInt("newSavedAds", newSavedAds);
        editor.apply();
    }

}
