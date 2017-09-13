package software.pipas.oprecox.modules.parsing;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import software.pipas.oprecox.modules.dataType.Ad;

public class AsyncGetAdd extends AsyncTask<Void, Void, Void>
{
    Ad ad = new Ad();
    Activity activity;
    boolean isFirst;
    String url;
    int index;

    public AsyncGetAdd(Activity act, String u, int i, boolean iF)
    {
        activity = act;
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
        /*try
        {
            ad.setPrice(OlxParser.getPrice(url));
            ad.setTitle(OlxParser.getTitle(url));
            ArrayList<String> images = OlxParser.getImage(url);
            ad.setImages(images);
            ArrayList<Bitmap> bms = new ArrayList<Bitmap>();
            for(int i = 0; i < images.size(); i++)
            {
                InputStream input = new java.net.URL(images.get(i)).openStream();
                bms.ad(BitmapFactory.decodeStream(input));
            }
            ad.setBmImages(bms);
            ad.setDescription(OlxParser.getDescription(url));
            ad.setUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {


        Log.d("ASYNC", String.format("Finished background async parse %d", index + 1));
    }
}
