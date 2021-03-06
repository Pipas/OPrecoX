package software.pipas.oprecox.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;


public abstract class Util
{
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public static Bitmap bitmapToThumbnail(Bitmap bitmap, int thumbnailSize, DisplayMetrics displayMetrics)
    {
        float dpPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, thumbnailSize, displayMetrics);
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
                else if(networkInterface.getName().contains("ap"))
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

    public static InetAddress listMyIP()
    {
        try
        {
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();

            for (Enumeration<NetworkInterface> e = list; e.hasMoreElements();)
            {
                NetworkInterface networkInterface = e.nextElement();
                Log.d("NETWORK_INTERFACES_LIST", networkInterface.toString());

                if(networkInterface.getName().contains("wlan"))
                {
                    List<InterfaceAddress> addressEnumeration = networkInterface.getInterfaceAddresses();
                    for (int i = 0; i < addressEnumeration.size(); i++)
                    {
                        InetAddress address = addressEnumeration.get(i).getAddress();
                        Log.d("NETWORK_INTERFACES_LIST", address.toString());

                        if(address instanceof Inet4Address)
                        {
                            return address;
                        }
                    }
                }
                else if(networkInterface.getName().contains("ap"))
                {
                    List<InterfaceAddress> addressEnumeration = networkInterface.getInterfaceAddresses();
                    for (int i = 0; i < addressEnumeration.size(); i++)
                    {
                        InetAddress address = addressEnumeration.get(i).getAddress();
                        Log.d("NETWORK_INTERFACES_LIST", address.toString());

                        if(address instanceof Inet4Address)
                        {
                            return address;
                        }
                    }
                }
            }

            return null;

        }
        catch (SocketException e)
        {
            Log.d("MY_IP", "failed to get");
            return null;
        }
    }

    public static String substituteSpace(String name)
    {
        return name.replaceAll("\\s", "_");
    }

    public static String substituteUnder(String name)
    {
        return name.replaceAll("_", " ");
    }

    public static int getStatusBarHeight(Resources resources)
    {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void interfaceListing()
    {
        try
        {
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();

            for (Enumeration<NetworkInterface> e = list; e.hasMoreElements();)
            {
                NetworkInterface networkInterface = e.nextElement();
                Log.d("NETWORK_INTERFACES_LIST", networkInterface.toString());

                List<InterfaceAddress> addressEnumeration = networkInterface.getInterfaceAddresses();
                for (int i = 0; i < addressEnumeration.size(); i++)
                {
                    InetAddress address = addressEnumeration.get(i).getAddress();
                    Log.d("NETWORK_INTERFACES_LIST", address.toString());

                }


                /*
                if(networkInterface.getName().contains("wlan"))
                {
                    List<InterfaceAddress> addressEnumeration = networkInterface.getInterfaceAddresses();
                    for (int i = 0; i < addressEnumeration.size(); i++)
                    {
                        InetAddress address = addressEnumeration.get(i).getAddress();
                        Log.d("NETWORK_INTERFACES_LIST", address.toString());

                    }
                }
                */
            }


        }
        catch (SocketException e)
        {
            Log.d("NETWORK_INTERFACES_LIST", "failed to get");
        }
    }
}


