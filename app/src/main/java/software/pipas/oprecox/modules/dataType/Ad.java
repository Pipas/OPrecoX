package software.pipas.oprecox.modules.dataType;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Ad
{
    private String title;
    private String description;
    private float price = -1;
    private ArrayList<Bitmap> images;
    private String url;
    private String category;
    private int scoreGained;

    public Ad()
    {
        scoreGained = 0;
    }

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

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String s)
    {
        url = s;
    }

    public void setImages(ArrayList<Bitmap> i)
    {
        images = i;
    }

    public ArrayList<Bitmap> getImages()
    {
        return images;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getScoreGained()
    {
        return scoreGained;
    }

    public void setScoreGained(int scoreGained)
    {
        this.scoreGained = scoreGained;
    }
}
