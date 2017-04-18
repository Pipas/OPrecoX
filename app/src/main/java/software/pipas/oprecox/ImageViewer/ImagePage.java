package software.pipas.oprecox.ImageViewer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import software.pipas.oprecox.R;

/**
 * Created by Pipas_ on 15/04/2017.
 *
 */

public class ImagePage extends Fragment
{
    // Store instance variables
    private String url;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static ImagePage newInstance(int page, String u) {
        ImagePage imagePage = new ImagePage();
        imagePage.setPage(page);
        imagePage.setUrl(u);
        return imagePage;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_page_layout, container, false);
        WebView wv = (WebView) view.findViewById(R.id.pageWebview);


        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.setScrollbarFadingEnabled(true);
        wv.setBackgroundColor(Color.parseColor("#000000"));
        wv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        wv.loadDataWithBaseURL(null, "<body style='margin:0;padding:0;'><img src=\"" + url + ".png\" width=\"100%\"/>", "text/html", "utf-8", null);

        return view;
    }

    public void setUrl(String u)
    {
        url = u;
    }

    public void setPage(int p)
    {
        page = p;
    }
}
