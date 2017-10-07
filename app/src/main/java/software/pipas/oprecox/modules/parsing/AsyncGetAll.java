package software.pipas.oprecox.modules.parsing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.categories.SubCategory;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.exceptions.OLXSyntaxChangeException;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;

public class AsyncGetAll extends AsyncTask<Void, Void, Void>
{
    private Ad ad = new Ad();
    private ParsingCallingActivity activity;
    private OPrecoX app;
    private int index;
    private boolean validURL = false;
    private boolean olxchange = false;
    private OlxParser olxParser;

    public AsyncGetAll(ParsingCallingActivity activity, OPrecoX app, int index, OlxParser olxParser)
    {
        this.activity = activity;
        this.app = app;
        this.index = index;
        this.olxParser = olxParser;
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
        String randomURL = "";
        SubCategory subCategory;
        while (!validURL)
        {
            try
            {
                subCategory = setSubCategory();
                randomURL = olxParser.getRandomURL(subCategory.getUrlEnd());

                getText(randomURL);

                getImages(randomURL);

                ad.setUrl(randomURL);

                validURL = true;
            }
            catch (NumberFormatException e)
            {
                Log.d("PARSE", "Exception caught async parse " + index + " '" + randomURL + "'");
            }
            catch (IOException e)
            {
                Log.d("PARSE", "Exception caught async parse " + index + " '" + randomURL + "'");
            }
            catch(OLXSyntaxChangeException e)
            {
                Log.d("PARSE", "Olx change async parse " + index + " '" + randomURL + "'");
                olxchange = true;
                return null;
            }
        }
        return null;
    }

    private void getImages(String randomURL) throws IOException
    {
        ArrayList<String> imageUrls = olxParser.getImageUrls(randomURL);
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
        ad.setDescription(olxParser.getDescription(randomURL));
        ad.setTitle(olxParser.getTitle(randomURL));
        ad.setPrice(olxParser.getPrice(randomURL));
    }

    private SubCategory setSubCategory()
    {
        SubCategory subCategory;
        subCategory = CategoryHandler.getRandomSubCategory();
        ad.setCategory(subCategory.getParentCategory());
        return subCategory;
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
            Log.d("PARSE", String.format("Finished async parse " + index +": '" + ad.getUrl() +"'"));
        }
    }
}
