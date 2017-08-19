package software.pipas.oprecox.application;

import android.app.Application;

import software.pipas.oprecox.modules.dataType.Ad;

public class OPrecoX extends Application
{
    private Ad tempAd;

    private Ad Ads[];

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
