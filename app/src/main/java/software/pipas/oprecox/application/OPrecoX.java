package software.pipas.oprecox.application;

import android.app.Application;
import android.content.Intent;

import software.pipas.oprecox.modules.dataType.Ad;

public class OPrecoX extends Application
{
    public static boolean announcing = false;

    private Ad Ads[];

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public Ad[] getAds()
    {
        return Ads;
    }

    public Ad getAd(int index)
    {
        return Ads[index];
    }

    public void setAds(Ad[] ads)
    {
        Ads = ads;
    }

    public void addAd(Ad ad, int index)
    {
        Ads[index] = ad;
    }

}
