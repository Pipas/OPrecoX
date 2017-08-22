package software.pipas.oprecox.activities.singlePlayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.ImagePagerAdapter;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.imageViewer.ImageViewer;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.listeners.OnSwipeTouchListener;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;

public class RevampedGameActivity extends AppCompatActivity implements ParsingCallingActivity
{
    private View guesserLayout;
    private BottomSheetBehavior slideupGuesser;
    private ImageView guessTag;
    private View guessTab;
    protected ViewPager imagePreview;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private PageIndicatorView imagePreviewIndicator;

    protected OPrecoX app;

    protected int gameSize;
    protected int score;
    protected int adIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revamped_game);

        initiateViews();

        setSlideupGuesserListener();
        setGuessTabListeners(true);

        setViewsWithAd(app.getAd(adIndex));
    }

    protected void setViewsWithAd(Ad ad)
    {
        titleTextView.setText(ad.getTitle());
        descriptionTextView.setText(ad.getDescription());

        ImagePagerAdapter adapterViewPager = new ImagePagerAdapter(getSupportFragmentManager(), ad.getImages(), this);
        imagePreview.setAdapter(adapterViewPager);

        if(ad.getImages().size() == 1)
            imagePreviewIndicator.setVisibility(View.GONE);
        else
            imagePreviewIndicator.setVisibility(View.VISIBLE);
        imagePreviewIndicator.setViewPager(imagePreview);
        imagePreviewIndicator.setSelection(0);
    }

    private void initiateViews()
    {
        guesserLayout = findViewById(R.id.guesserLayout);
        guessTag = (ImageView) findViewById(R.id.guessTag);
        guessTab = findViewById(R.id.guessTab);

        imagePreview = (ViewPager) findViewById(R.id.imagePreview);
        imagePreviewIndicator = (PageIndicatorView) findViewById(R.id.imagePreviewIndicator);

        imagePreviewIndicator.setAnimationType(AnimationType.SCALE);
        imagePreviewIndicator.setRadius(5);
        imagePreviewIndicator.setSelectedColor(Color.parseColor("#FFCA72"));

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        app = (OPrecoX) getApplicationContext();
    }

    private void setSlideupGuesserListener()
    {
        slideupGuesser = BottomSheetBehavior.from(guesserLayout);
        slideupGuesser.setPeekHeight(0);
        slideupGuesser.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    setGuessTabListeners(true);
                }
                else
                {
                    setGuessTabListeners(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });
    }

    private void setGuessTabListeners(boolean active)
    {
        if(active)
        {
            guessTab.setOnTouchListener(new OnSwipeTouchListener(RevampedGameActivity.this)
            {
                public void onSwipeTop()
                {
                    togglePanel(guessTab);
                }

                public void onSingleTap()
                {
                    togglePanel(guessTag);
                }
            });
            guessTag.setOnTouchListener(new OnSwipeTouchListener(RevampedGameActivity.this)
            {
                public void onSwipeTop()
                {
                    togglePanel(guessTag);
                }

                public void onSingleTap()
                {
                    togglePanel(guessTag);
                }
            });
        }
        else
        {
            guessTab.setOnTouchListener(null);
            guessTag.setOnTouchListener(null);
        }
    }

    public void togglePanel(View v)
    {
        if(slideupGuesser.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            slideupGuesser.setState(BottomSheetBehavior.STATE_EXPANDED);
        else
            slideupGuesser.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    protected void startDataParses()
    {
        AsyncGetAll parsingAyncTask;
        for(int i = 2; i < gameSize; i++)
        {
            parsingAyncTask = new AsyncGetAll(this, app, i);
            parsingAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void startImageViewerActivity(int page)
    {
        Intent myIntent = new Intent(this, ImageViewer.class);
        myIntent.putExtra("page", page);
        myIntent.putExtra("adIndex", adIndex);
        startActivityForResult(myIntent, 1);
    }

    @Override
    public void onBackPressed()
    {
        if(slideupGuesser.getState() == BottomSheetBehavior.STATE_EXPANDED)
            slideupGuesser.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
        {
            AlertDialog.Builder popup = new AlertDialog.Builder(this, R.style.DialogTheme);
            popup.setTitle(getString(R.string.leavegame));
            popup.setMessage(getString(R.string.leavegamesub));
            popup.setCancelable(true);

            popup.setPositiveButton(
                    R.string.leave,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            popup.setNegativeButton(
                    R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = popup.create();
            alert.show();
        }
    }

    @Override
    public void parsingEnded()
    {

    }
}
