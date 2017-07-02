package software.pipas.oprecox.modules.imageViewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import software.pipas.oprecox.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.FullscreenPagerAdapter;

public class ImageViewer extends AppCompatActivity
{
    FullscreenPagerAdapter adapterViewPager;
    ArrayList<Bitmap> images;
    int page;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        OPrecoX app = (OPrecoX) getApplicationContext();
        images = app.getBitmaps();

        Intent myIntent = getIntent();
        page = myIntent.getIntExtra("page", 0);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new FullscreenPagerAdapter(getSupportFragmentManager(), images);
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        if(images.size() == 1)
            indicator.setVisibility(View.GONE);
        else
            indicator.setVisibility(View.VISIBLE);
        indicator.setViewPager(vpPager);

        vpPager.setCurrentItem(page);
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("page", vpPager.getCurrentItem());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
