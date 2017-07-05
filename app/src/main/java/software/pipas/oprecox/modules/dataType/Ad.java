package software.pipas.oprecox.modules.dataType;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ad implements Parcelable
{
    private String title;
    private String description;
    private float price = -1;
    private ArrayList<Bitmap> images;
    private String url;

    public Ad() {}

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

    protected Ad(Parcel in)
    {
        title = in.readString();
        description = in.readString();
        price = in.readFloat();
        if (in.readByte() == 0x01)
        {
            images = new ArrayList<Bitmap>();
            in.readList(images, Bitmap.class.getClassLoader());
        }
        else
        {
            images = null;
        }
        url = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeFloat(price);
        if (images == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeList(images);
        }
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ad> CREATOR = new Parcelable.Creator<Ad>()
    {
        @Override
        public Ad createFromParcel(Parcel in)
        {
            return new Ad(in);
        }

        @Override
        public Ad[] newArray(int size)
        {
            return new Ad[size];
        }
    };

}
