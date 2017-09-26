package software.pipas.oprecox.util;

import android.app.Activity;
import android.content.SharedPreferences;

import java.net.Inet4Address;

import static android.content.Context.MODE_PRIVATE;

public abstract class Settings
{
    private static int gameSize;
    private static int adCountdown;
    private static int gamesPlayed;
    private static Boolean showRateUs;
    private static String customName;

    private static Inet4Address lastIP;

    public static int getGameSize()
    {
        return gameSize;
    }

    public static void setGameSize(int gS, Activity activity)
    {
        if(gS == gameSize)
            return;
        gameSize = gS;
        SharedPreferences.Editor editor = activity.getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
        editor.putInt("gameSize", gameSize);
        editor.apply();
    }

    public static int getAdCountdown()
    {
        return adCountdown;
    }

    public static void setAdCountdown(int adCountdown)
    {
        Settings.adCountdown = adCountdown;
    }

    public static void updateAdCountdown(Activity activity)
    {
        if(Settings.adCountdown == 0)
            Settings.adCountdown = 3;
        else
            Settings.adCountdown = adCountdown - 1;

        SharedPreferences.Editor editor = activity.getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
        editor.putInt("adCountdown", Settings.adCountdown);
        editor.apply();
    }

    public static void setLastIP(Inet4Address newIP)
    {
        lastIP = newIP;
    }

    public static Inet4Address getLastIP()
    {
        return lastIP;
    }

    public static int getGamesPlayed()
    {
        return gamesPlayed;
    }

    public static void setGamesPlayed(int gamesPlayed)
    {
        Settings.gamesPlayed = gamesPlayed;
    }

    public static void increaseGamesPlayed(Activity activity)
    {
        Settings.gamesPlayed++;
        SharedPreferences.Editor editor = activity.getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
        editor.putInt("gamesPlayed", Settings.gamesPlayed);
        editor.apply();
    }

    public static Boolean getShowRateUs()
    {
        return showRateUs;
    }

    public static void setShowRateUs(Boolean showRateUs)
    {
        Settings.showRateUs = showRateUs;
    }

    public static String getCustomName()
    {
        return customName;
    }

    public static void setCustomName(String customName)
    {
        Settings.customName = customName;
    }
}
