package software.pipas.oprecox.modules.adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import software.pipas.oprecox.activities.singlePlayer.GameActivity;
import software.pipas.oprecox.modules.imageViewer.SmallImagePage;

public class ImagePagerAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<Bitmap> images;
    private GameActivity gameActivity;

    public ImagePagerAdapter(FragmentManager fragmentManager, ArrayList<Bitmap> images, GameActivity gameActivity)
    {
        super(fragmentManager);
        this.images = images;
        this.gameActivity = gameActivity;
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
            currentFragment = SmallImagePage.newInstance(position, images.get(position), gameActivity);
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
