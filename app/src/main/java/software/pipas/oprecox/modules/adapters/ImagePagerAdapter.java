package software.pipas.oprecox.modules.adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import software.pipas.oprecox.modules.imageViewer.SmallImagePage;

import static android.os.Build.VERSION_CODES.M;

public class ImagePagerAdapter extends FragmentPagerAdapter
{
    private ArrayList<Bitmap> images;
    private Fragment[] mFragments;

    public ImagePagerAdapter(FragmentManager fragmentManager, ArrayList<Bitmap> img)
    {
        super(fragmentManager);
        images = img;
        mFragments = new Fragment[images.size()];
        Log.d("SIZE", "size of images is " + images.size());
    }

    @Override
    public int getCount()
    {
        return mFragments.length;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment frag = mFragments[position];
        if (frag == null)
        {
            frag = SmallImagePage.newInstance(position, images.get(position));
            mFragments[position] = frag;
        }
        return frag;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "Page " + position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Object ret = super.instantiateItem(container, position);
        mFragments[position] = (Fragment) ret;
        return ret;
    }
}
