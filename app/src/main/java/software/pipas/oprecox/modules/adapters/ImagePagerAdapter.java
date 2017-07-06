package software.pipas.oprecox.modules.adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import software.pipas.oprecox.modules.imageViewer.SmallImagePage;

public class ImagePagerAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<Bitmap> images;

    public ImagePagerAdapter(FragmentManager fragmentManager, ArrayList<Bitmap> img)
    {
        super(fragmentManager);
        images = img;
        Log.d("SIZE", "size of images is " + images.size());
    }

    @Override
    public int getCount()
    {
        return images.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment currentFragment = null;

        if((images != null) && (images.size() > 0) && (position >= 0) && (position < images.size()))
        {
            currentFragment = SmallImagePage.newInstance(position, images.get(position));
            currentFragment.setRetainInstance(true);
        }

        return currentFragment;

    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "Page " + position;
    }
}
