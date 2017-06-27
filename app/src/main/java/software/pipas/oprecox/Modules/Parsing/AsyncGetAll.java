package software.pipas.oprecox.modules.parsing;

/**
 * Created by Pipas_ on 13/04/2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import software.pipas.oprecox.activities.singlePlayer.GameActivity;
import software.pipas.oprecox.modules.add.Add;

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

    public AsyncGetAll(GameActivity act, int i, boolean iF)
    {
        activity = act;
        isFirst = iF;
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
                randomURL = OlxParser.getRandomURL();
                Log.d("URL", randomURL);
                try
                {
                    add.setPrice(OlxParser.getPrice(randomURL));
                    add.setTitle(OlxParser.getTitle(randomURL));
                    ArrayList<String> images = OlxParser.getImage(randomURL);
                    add.setImages(images);
                    ArrayList<Bitmap> bms = new ArrayList<Bitmap>();
                    for(int i = 0; i < images.size(); i++)
                    {
                        InputStream input = new java.net.URL(images.get(i)).openStream();
                        bms.add(BitmapFactory.decodeStream(input));
                    }
                    add.setBmImages(bms);
                    add.setDescription(OlxParser.getDescription(randomURL));
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
            activity.addAdd(add);

        Log.d("ASYNC", String.format("Finished background async parse %d", index + 1));
    }
}
