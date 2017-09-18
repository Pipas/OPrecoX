package software.pipas.oprecox.modules.parsing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.exceptions.OLXSyntaxChangeException;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;

public class AsyncGetAdd extends AsyncTask<Void, Void, Void>
{
    private Ad ad = new Ad();
    private ParsingCallingActivity activity;
    private String url;
    private int index;
    private OPrecoX app;
    private Boolean validURL;
    private boolean olxchange = false;

    public AsyncGetAdd(ParsingCallingActivity activity, OPrecoX app, String url, int index)
    {
        this.activity = activity;
        this.app = app;
        this.index = index;
        this.url = url;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        Log.d("PARSE", "Started background async parse");
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        while (!validURL)
        {
            try
            {
                getText(url);

                getImages(url);

                ad.setUrl(url);

                validURL = true;
            }
            catch (NumberFormatException e)
            {
                Log.d("PARSE", "Exception caught invalid url '" + url + "'");
            }
            catch (IOException e)
            {
                Log.d("PARSE", "Exception caught in url '" + url + "'");
            }
            catch(OLXSyntaxChangeException e)
            {
                Log.d("PARSE", "OLX changed in url '" + url + "'");
                olxchange = true;
                return null;
            }
        }
        return null;
    }

    private void getImages(String randomURL) throws IOException
    {
        ArrayList<String> imageUrls = OlxParser.getImageUrls(randomURL);
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();

        int imageMax;
        if(imageUrls.size() < 8)
            imageMax = imageUrls.size();
        else
            imageMax = 8;

        for(int i = 0; i < imageMax; i++)
        {
            InputStream input = new java.net.URL(imageUrls.get(i)).openStream();
            images.add(BitmapFactory.decodeStream(input));
        }
        ad.setImages(images);
    }

    private void getText(String randomURL) throws IOException, OLXSyntaxChangeException
    {
        ad.setDescription(OlxParser.getDescription(randomURL));
        ad.setTitle(OlxParser.getTitle(randomURL));
        ad.setPrice(OlxParser.getPrice(randomURL));
    }


    @Override
    protected void onPostExecute(Void result)
    {
        if(olxchange)
            activity.olxChangeException();
        else
        {
            app.addAd(ad, index);
            activity.parsingEnded();
            Log.d("PARSE", String.format("Finished background async parse '" + ad.getUrl() +"'"));
        }
    }
}
