package software.pipas.oprecox.modules.parsing;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.categories.SubCategory;
import software.pipas.oprecox.modules.exceptions.OLXSyntaxChangeException;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;

public class AsyncGetURL extends AsyncTask<Void, Void, Void>
{
    private ParsingCallingActivity activity;
    private int index;
    private boolean validURL = false;
    private boolean olxchange = false;
    private String url;

    public AsyncGetURL(ParsingCallingActivity activity, OPrecoX app, int index)
    {
        this.activity = activity;
        this.index = index;
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
                url = OlxParser.getRandomURL(subCategory.getUrlEnd());

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

    private SubCategory setSubCategory()
    {
        SubCategory subCategory;
        subCategory = CategoryHandler.getRandomSubCategory();
        return subCategory;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        Log.d("PARSE", String.format("Finished background async parse '" + url +"'"));
    }
}