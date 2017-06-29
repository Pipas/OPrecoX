package software.pipas.oprecox.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.activities.other.BlockedApp;

import static android.content.Context.MODE_PRIVATE;


public abstract class Util
{
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void lockApp(Activity activity)
    {
        SharedPreferences.Editor editor = activity.getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
        editor.putBoolean("locked", true);
        editor.putInt("lockVersion", BuildConfig.VERSION_CODE);
        editor.apply();
        Intent intent = new Intent(activity, BlockedApp.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
