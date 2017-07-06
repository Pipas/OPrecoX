package software.pipas.oprecox.modules.imageViewer;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.R;

import me.relex.circleindicator.CircleIndicator;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.FullscreenPagerAdapter;
import software.pipas.oprecox.modules.dataType.Ad;

public class ImageViewer extends AppCompatActivity
{
    FullscreenPagerAdapter adapterViewPager;
    Ad ad;
    int page;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        OPrecoX app = (OPrecoX) getApplicationContext();
        ad = app.getTempAd();

        Intent myIntent = getIntent();
        page = myIntent.getIntExtra("page", 0);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new FullscreenPagerAdapter(getSupportFragmentManager(), ad.getImages(), vpPager);
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        if(ad.getImages().size() == 1)
            indicator.setVisibility(View.GONE);
        else
            indicator.setVisibility(View.VISIBLE);
        indicator.setViewPager(vpPager);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(ad.getTitle());

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
