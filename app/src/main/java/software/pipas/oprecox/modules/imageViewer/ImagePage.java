package software.pipas.oprecox.modules.imageViewer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;

import software.pipas.oprecox.R;

public class ImagePage extends Fragment
{
    private Bitmap image;
    private int page;
    private ViewPager viewPager;

    public static ImagePage newInstance(int page, Bitmap i, ViewPager vp)
    {
        ImagePage imagePage = new ImagePage();
        imagePage.setPage(page);
        imagePage.setImage(i);
        imagePage.setViewPager(vp);
        return imagePage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.image_page_layout, container, false);
        GestureImageView gImageView = (GestureImageView) view.findViewById(R.id.gImageView);
        gImageView.setImageBitmap(image);
        gImageView.getController().enableScrollInViewPager(viewPager);
        gImageView.getController().getSettings()
                .setMaxZoom(4f)
                .setDoubleTapZoom(-1f)
                .setPanEnabled(true)
                .setZoomEnabled(true)
                .setDoubleTapEnabled(true)
                .setRotationEnabled(false)
                .setRestrictRotation(false)
                .setOverscrollDistance(0f, 0f)
                .setOverzoomFactor(2f)
                .setFillViewport(false)
                .setFitMethod(Settings.Fit.HORIZONTAL)
                .setGravity(Gravity.CENTER_VERTICAL);

        return view;
    }

    public void setImage(Bitmap i)
    {
        image = i;
    }

    public void setPage(int p)
    {
        page = p;
    }

    public void setViewPager(ViewPager vp)
    {
        viewPager = vp;
    }
}
