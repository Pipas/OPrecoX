package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.ImagePagerAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.fragments.GameDataFragment;
import software.pipas.oprecox.modules.imageViewer.ImageViewer;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.listeners.OnSwipeTouchListener;

public class GameActivity extends AppCompatActivity implements ParsingCallingActivity
{
    private View guesserLayout;
    private BottomSheetBehavior slideupGuesser;
    private ImageView guessTag;
    private View guessTab;
    protected ViewPager imagePreview;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private PageIndicatorView imagePreviewIndicator;
    private Boolean blocked = false;

    private GameDataFragment gameDataFragment;

    protected OPrecoX app;

    protected int gameSize;
    protected int score;
    protected int adIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentManager fm = getFragmentManager();
        gameDataFragment = (GameDataFragment) fm.findFragmentByTag("gamedata");

        initiateViews();

        initiateCustomFonts();

        setSlideupGuesserListener();
        setGuessTabListeners(true);

        if (gameDataFragment == null)
        {
            gameDataFragment = new GameDataFragment();
            fm.beginTransaction().add(gameDataFragment, "gamedata").commit();

            setViewsWithAd(app.getAd(adIndex));
        }
        else
        {
            gameSize = gameDataFragment.getGameSize();
            score = gameDataFragment.getScore();
            //correctGuesses = gameDataFragment.getCorrectGuesses();
            adIndex = gameDataFragment.getAdIndex();
            app.setAds(gameDataFragment.getAds());

            setViewsWithAd(app.getAd(adIndex));
        }
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
        imagePreviewIndicator.setSelectedColor(Color.parseColor("#EF913F"));

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        app = (OPrecoX) getApplicationContext();
    }

    private void initiateCustomFonts()
    {
        CustomFontHelper.setCustomFont(titleTextView, "font/antipastopro-demibold.otf", getBaseContext());
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
            guessTab.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this)
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
            guessTag.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this)
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

    @Override
    public void olxChangeException()
    {
        if(!blocked)
        {
            blocked = true;
            Intent myIntent = new Intent(this, BlockedApp.class);
            startActivity(myIntent);
            finish();
        }
    }

    @Override
    public void onDestroy()
    {
        Log.d("DEBUG", "Im being destroyed");
        super.onDestroy();
        gameDataFragment.setData(gameSize, score, 0, adIndex, app.getAds());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                int page = data.getIntExtra("page", 0);
                imagePreview.setCurrentItem(page);
            }
        }
    }
}
