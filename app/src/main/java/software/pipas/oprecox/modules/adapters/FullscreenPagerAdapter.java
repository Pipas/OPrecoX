package software.pipas.oprecox.modules.adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import software.pipas.oprecox.modules.imageViewer.ImagePage;

public class FullscreenPagerAdapter extends FragmentStatePagerAdapter
{
    private int NUM_ITEMS;
    private ArrayList<Bitmap> images;
    private ViewPager viewPager;

    public FullscreenPagerAdapter(FragmentManager fragmentManager, ArrayList<Bitmap> img, ViewPager vp)
    {
        super(fragmentManager);
        NUM_ITEMS = img.size();
        images = img;
        viewPager = vp;
    }

    @Override
    public int getCount()
    {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position)
    {
        return ImagePage.newInstance(position, images.get(position), viewPager);
    }
}
