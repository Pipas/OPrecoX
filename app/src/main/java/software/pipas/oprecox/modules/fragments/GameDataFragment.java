package software.pipas.oprecox.modules.fragments;

import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;

import software.pipas.oprecox.modules.dataType.Ad;

public class GameDataFragment extends Fragment
{
    private int NGUESSES;
    private int score;
    private int correctGuesses;
    private ArrayList<Ad> ads;
    private Ad shownAd;
    private float guess;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setAds(ArrayList<Ad> ads)
    {
        this.ads = ads;
    }

    public ArrayList<Ad> getAds()
    {
        return ads;
    }

    public Ad getShownAd()
    {
        return shownAd;
    }

    public void setShownAd(Ad shownAd)
    {
        this.shownAd = shownAd;
    }

    public int getNGUESSES()
    {
        return NGUESSES;
    }

    public void setNGUESSES(int NGUESSES)
    {
        this.NGUESSES = NGUESSES;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getCorrectGuesses()
    {
        return correctGuesses;
    }

    public void setCorrectGuesses(int correctGuesses)
    {
        this.correctGuesses = correctGuesses;
    }

    public float getGuess()
    {
        return guess;
    }

    public void setGuess(float guess)
    {
        this.guess = guess;
    }

    public void setData(int NGUESSES, int score, int correctGuesses, ArrayList<Ad> ads, Ad shownAd, float guess)
    {
        this.NGUESSES = NGUESSES;
        this.score = score;
        this.correctGuesses = correctGuesses;
        this.ads = ads;
        this.shownAd = shownAd;
        this.guess = guess;
    }
}
