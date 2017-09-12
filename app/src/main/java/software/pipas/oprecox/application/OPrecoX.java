package software.pipas.oprecox.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import software.pipas.oprecox.modules.dataType.Ad;

public class OPrecoX extends Application
{
    Ad tempAd;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public void storeAd(Ad ad)
    {
        this.tempAd = ad;
    }

    public Ad getTempAd()
    {
        return tempAd;
    }

}
