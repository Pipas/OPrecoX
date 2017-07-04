package software.pipas.oprecox.modules.adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import software.pipas.oprecox.modules.imageViewer.SmallImagePage;

public class ImagePagerAdapter extends FragmentStatePagerAdapter
{
    private int NUM_ITEMS;
    private ArrayList<Bitmap> images;

    public ImagePagerAdapter(FragmentManager fragmentManager, ArrayList<Bitmap> img) {
        super(fragmentManager);
        NUM_ITEMS = img.size();
        images = img;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position)
    {
        return SmallImagePage.newInstance(position, images.get(position));
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
