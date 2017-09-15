package software.pipas.oprecox.modules.fragments;

import android.app.Fragment;
import android.os.Bundle;

import software.pipas.oprecox.modules.dataType.Ad;

public class GameDataFragment extends Fragment
{
    private int gameSize;
    private int adIndex;
    private int score;
    private Ad[] ads;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public int getGameSize()
    {
        return gameSize;
    }

    public void setGameSize(int gameSize)
    {
        this.gameSize = gameSize;
    }

    public int getAdIndex()
    {
        return adIndex;
    }

    public void setAdIndex(int adIndex)
    {
        this.adIndex = adIndex;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public Ad[] getAds()
    {
        return ads;
    }

    public void setAds(Ad[] ads)
    {
        this.ads = ads;
    }

    public void setData(int gameSize, int score, int adIndex, Ad[] ads)
    {
        this.gameSize = gameSize;
        this.score = score;
        this.ads = ads;
        this.adIndex = adIndex;
    }
}
