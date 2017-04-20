package software.pipas.oprecox.Adds;

/**
 * Created by Pipas_ on 13/04/2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import software.pipas.oprecox.GameActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AsyncGetAdd extends AsyncTask<Void, Void, Void>
{
    Add add = new Add();
    GameActivity activity;
    OlxParser parser;
    boolean isFirst;
    String url;
    int index;

    public AsyncGetAdd(GameActivity act, String u, int i, boolean iF)
    {
        activity = act;
        parser  = new OlxParser();
        isFirst = iF;
        url = u;
        index = i;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        Log.d("ASYNC", "Started background async parse");
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            add.setPrice(parser.getPrice(url));
            add.setTitle(parser.getTitle(url));
            ArrayList<String> images = parser.getImage(url);
            add.setImages(images);
            ArrayList<Bitmap> bms = new ArrayList<Bitmap>();
            for(int i = 0; i < images.size(); i++)
            {
                InputStream input = new java.net.URL(images.get(i)).openStream();
                bms.add(BitmapFactory.decodeStream(input));
            }
            add.setBmImages(bms);
            add.setDescription(parser.getDescription(url));
            add.setUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if(isFirst)
        {
            activity.setShownAdd(add);
            activity.closeProgressPopup();
        }
        else
            activity.addAddPosition(add, index);

        Log.d("ASYNC", String.format("Finished background async parse %d", index + 1));
    }
}
