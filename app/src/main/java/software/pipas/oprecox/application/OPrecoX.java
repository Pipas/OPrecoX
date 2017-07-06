package software.pipas.oprecox.application;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

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
