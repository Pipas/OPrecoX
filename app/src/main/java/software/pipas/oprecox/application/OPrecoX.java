package software.pipas.oprecox.application;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class OPrecoX extends Application
{
    ArrayList<Bitmap> bitmaps = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public void storeBitmaps(ArrayList<Bitmap> bm)
    {
        this.bitmaps = bm;
    }

    public ArrayList<Bitmap> getBitmaps()
    {
        return bitmaps;
    }
}
