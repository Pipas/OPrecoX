package software.pipas.oprecox.ImageViewer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Pipas_ on 16/04/2017.
 */

public class FullscreenPagerAdapter extends FragmentStatePagerAdapter {
    private int NUM_ITEMS;
    private ArrayList<String> images;

    public FullscreenPagerAdapter(FragmentManager fragmentManager, ArrayList<String> img) {
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
        return ImagePage.newInstance(position, images.get(position));
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
