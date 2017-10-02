package software.pipas.oprecox.modules.parsing;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.categories.SubCategory;
import software.pipas.oprecox.modules.exceptions.OLXSyntaxChangeException;
import software.pipas.oprecox.modules.interfaces.OnUrlLoaded;

public class AsyncGetUrl extends AsyncTask<Void, Void, Void>
{
    private OnUrlLoaded onUrlLoaded;
    private boolean validURL = false;
    private boolean olxchange = false;
    private String url;
    private OlxParser olxParser;

    public AsyncGetUrl(OnUrlLoaded onUrlLoaded, OlxParser olxParser)
    {
        this.onUrlLoaded = onUrlLoaded;
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
        url = "";
        SubCategory subCategory;
        while (!validURL)
        {
            try
            {
                subCategory = setSubCategory();
                url = olxParser.getRandomURL(subCategory.getUrlEnd());

                validURL = olxParser.isValid(url);
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

    private SubCategory setSubCategory()
    {
        SubCategory subCategory;
        subCategory = CategoryHandler.getRandomSubCategory();
        return subCategory;

    }

    @Override
    protected void onPostExecute(Void result)
    {
        Log.d("PARSE", String.format("Finished background async parse '" + url + "'"));
        if(validURL)
            this.onUrlLoaded.addAdUrl(url);
    }
}