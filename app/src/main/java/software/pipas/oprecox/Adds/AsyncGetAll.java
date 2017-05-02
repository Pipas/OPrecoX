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

public class AsyncGetAll extends AsyncTask<Void, Void, Void>
{
    Add add = new Add();
    GameActivity activity;
    boolean isFirst;
    int index;
    String url;
    boolean validURL = false;
    String randomURL = null;
    OlxParser parser;

    public AsyncGetAll(GameActivity act, ArrayList<String> c, int i, boolean iF)
    {
        activity = act;
        isFirst = iF;
        parser  = new OlxParser(c);
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
            while (!validURL)
            {
                randomURL = parser.getRandomURL();
                Log.d("URL", randomURL);
                try
                {
                    add.setPrice(parser.getPrice(randomURL));
                    add.setTitle(parser.getTitle(randomURL));
                    ArrayList<String> images = parser.getImage(randomURL);
                    add.setImages(images);
                    ArrayList<Bitmap> bms = new ArrayList<Bitmap>();
                    for(int i = 0; i < images.size(); i++)
                    {
                        InputStream input = new java.net.URL(images.get(i)).openStream();
                        bms.add(BitmapFactory.decodeStream(input));
                    }
                    add.setBmImages(bms);
                    add.setDescription(parser.getDescription(randomURL));
                    add.setUrl(randomURL);
                    validURL = true;
                }
                catch (NumberFormatException e)
                {
                    Log.d("PRICE", "Exception caught invalid url");
                }
            }
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
