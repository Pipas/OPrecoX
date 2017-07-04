package software.pipas.oprecox.modules.parsing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.exceptions.OLXSyntaxChangeException;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.util.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AsyncGetAll extends AsyncTask<Void, Void, Void>
{
    private Ad ad = new Ad();
    private ParsingCallingActivity activity;
    private boolean isFirst;
    private boolean validURL = false;

    public AsyncGetAll(ParsingCallingActivity pca, boolean iF)
    {
        activity = pca;
        isFirst = iF;
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
        String randomURL = "";
        while (!validURL)
        {
            try
            {
                randomURL = OlxParser.getRandomURL();
                ad.setPrice(OlxParser.getPrice(randomURL));
                ad.setTitle(OlxParser.getTitle(randomURL));
                ArrayList<String> imageUrls = OlxParser.getImageUrls(randomURL);
                ArrayList<Bitmap> images = new ArrayList<Bitmap>();
                for(int i = 0; i < imageUrls.size(); i++)
                {
                    InputStream input = new java.net.URL(imageUrls.get(i)).openStream();
                    images.add(BitmapFactory.decodeStream(input));
                }
                ad.setImages(images);
                ad.setDescription(OlxParser.getDescription(randomURL));
                ad.setUrl(randomURL);
                validURL = true;
            }
            catch (NumberFormatException e)
            {
                Log.d("PARSE", "Exception caught invalid url '" + randomURL + "'");
            }
            catch (IOException e)
            {
                Log.d("PARSE", "Exception caught in url '" + randomURL + "'");
            }
            catch(OLXSyntaxChangeException e)
            {
                Log.d("PARSE", "OLX changed in url '" + randomURL + "'");
                Settings.setLocked(true);
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if(isFirst)
        {
            activity.setShownAd(ad);
            activity.closeProgressPopup();
        }
        else
            activity.addAdd(ad);

        Log.d("ASYNC", String.format("Finished background async parse '" + ad.getUrl() +"'"));
    }
}
