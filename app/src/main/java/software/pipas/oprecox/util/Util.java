package software.pipas.oprecox.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

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

    public static byte[] bitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap biteArrayToBitmap(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap bitmapToThumbnail(Bitmap bitmap, int thumbnailSize)
    {
        float dpPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, thumbnailSize, Settings.getDeviceDisplayMetrics());
        return ThumbnailUtils.extractThumbnail(bitmap, (int) dpPixels, (int) dpPixels);
    }


    public static LinkedList<InetAddress> listinterfaces()
    {
        try
        {
            LinkedList<InetAddress> address_list = new LinkedList<InetAddress>();
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();

            for (Enumeration<NetworkInterface> e = list; e.hasMoreElements();)
            {
                //Log.d("NETWORK_INTERFACES_LIST", e.nextElement().getName());
                NetworkInterface networkInterface = e.nextElement();

                if(networkInterface.getName().contains("wlan"))
                {
                    List<InterfaceAddress> addressEnumeration = networkInterface.getInterfaceAddresses();

                    for (int i = 0; i < addressEnumeration.size(); i++)
                    {
                        InetAddress broadcast_address = addressEnumeration.get(i).getBroadcast();

                        if(broadcast_address != null)
                        {
                            //Log.d("NETWORK_INTERFACES_LIST", broadcast_address.toString());
                            address_list.add(broadcast_address);
                        }
                        else
                        {
                            //Log.d("NETWORK_INTERFACES_LIST", "null");
                        }
                    }
                }
            }

            return address_list;

        }
        catch (SocketException e)
        {
            Log.d("NETWORK_INTERFACES_LIST", "failed to get");
            return null;
        }

    }
}
