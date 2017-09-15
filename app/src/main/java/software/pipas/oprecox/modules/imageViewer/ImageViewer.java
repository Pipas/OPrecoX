package software.pipas.oprecox.modules.imageViewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.FullscreenPagerAdapter;
import software.pipas.oprecox.modules.dataType.Ad;

import static software.pipas.oprecox.R.id.indicator;

public class ImageViewer extends AppCompatActivity
{
    private FullscreenPagerAdapter adapterViewPager;
    private Ad ad;
    private int page;
    private int adIndex;
    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Intent myIntent = getIntent();
        page = myIntent.getIntExtra("page", 0);
        adIndex = myIntent.getIntExtra("adIndex", 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        OPrecoX app = (OPrecoX) getApplicationContext();
        ad = app.getAd(adIndex);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new FullscreenPagerAdapter(getSupportFragmentManager(), ad.getImages(), vpPager);
        vpPager.setAdapter(adapterViewPager);

        PageIndicatorView imagePreviewIndicator = (PageIndicatorView) findViewById(indicator);
        if(ad.getImages().size() == 1)
            imagePreviewIndicator.setVisibility(View.GONE);
        else
            imagePreviewIndicator.setVisibility(View.VISIBLE);
        imagePreviewIndicator.setAnimationType(AnimationType.SCALE);
        imagePreviewIndicator.setRadius(5);
        imagePreviewIndicator.setViewPager(vpPager);
        imagePreviewIndicator.setSelection(page);

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
