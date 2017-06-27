package software.pipas.oprecox.Modules.Add;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Pipas_ on 12/03/2017.
 */

public class Add
{
    private String title;
    private String description;
    private float price = -1;
    private ArrayList<Bitmap> bmImages;
    private String url;
    private ArrayList<String> images;

    public Add() {}

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    public ArrayList<Bitmap> getBmImages()
    {
        return bmImages;
    }

    public void setBmImages(ArrayList<Bitmap> i)
    {
        bmImages = i;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String s)
    {
        url = s;
    }

    public void setImages(ArrayList<String> i)
    {
        images = i;
    }

    public ArrayList<String> getImages()
    {
        return images;
    }
}
