package software.pipas.oprecox.modules.parsing;

/**
 * Created by Pipas_ on 13/04/2017.
 */
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class AsyncGetURL extends AsyncTask<Void, Void, Void>
{
    /*boolean validURL = false;
    String randomURL = null;
    OlxParser parser;

    public AsyncGetURL(MultiPlayerOptions act, ArrayList<String> c)
    {
        activity = act;
        parser  = new OlxParser(c);
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
                validURL = parser.isValid(randomURL);
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
        activity.endAsyncTask(randomURL);
        Log.d("ASYNC", "Finished background async parse");
    }*/

    @Override
    protected Void doInBackground(Void... params)
    {
        return null;
    }
}