package software.pipas.oprecox.activities.singlePlayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.ImagePagerAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.imageViewer.ImageViewer;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.listeners.OnSwipeTouchListener;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;
import software.pipas.oprecox.modules.parsing.OlxParser;

public class GameActivity extends AppCompatActivity implements ParsingCallingActivity
{
    private View guesserLayout;
    protected BottomSheetBehavior slideupGuesser;
    private ImageView guessTag;
    private View guessTab;
    protected ViewPager imagePreview;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private PageIndicatorView imagePreviewIndicator;
    private Boolean blocked = false;
    protected OlxParser olxParser;
    private View countdownTimer;
    private ValueAnimator countdownAnimation;

    protected OPrecoX app;
    private ArrayList<AsyncGetAll> asyncTasks;
    protected int gameSize;
    protected int score;
    protected static int adIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (OPrecoX) getApplicationContext();
        if(app.getAds() == null)
            return;

        setContentView(R.layout.activity_game);

        initiateViews();

        initiateCustomFonts();

        setSlideupGuesserListener();
        setGuessTabListeners(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setViewsWithAd(app.getAd(adIndex));

        startCountdownAnimation(5);
    }

    protected void startCountdownAnimation(int duration)
    {
        countdownAnimation = ValueAnimator.ofInt(countdownTimer.getMeasuredWidth(), getResources().getDisplayMetrics().widthPixels);
        countdownAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = countdownTimer.getLayoutParams();
                layoutParams.width = val;
                countdownTimer.setLayoutParams(layoutParams);
            }
        });
        countdownAnimation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                Log.d("ANIMATION", "Animation finished");
                finishActivity(1);
            }
        });
        countdownAnimation.setDuration(duration * 1000);
        countdownAnimation.start();
    }

    protected void resetCountdownView()
    {
        ViewGroup.LayoutParams layoutParams = countdownTimer.getLayoutParams();
        layoutParams.width = 0;
        countdownTimer.setLayoutParams(layoutParams);
    }

    protected void stopCountdown()
    {
        countdownAnimation.cancel();
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

        countdownTimer = findViewById(R.id.countdownTimer);

        imagePreview = (ViewPager) findViewById(R.id.imagePreview);
        imagePreviewIndicator = (PageIndicatorView) findViewById(R.id.imagePreviewIndicator);

        imagePreviewIndicator.setAnimationType(AnimationType.SCALE);
        imagePreviewIndicator.setRadius(5);
        imagePreviewIndicator.setSelectedColor(Color.parseColor("#FFCA72"));

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
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

    protected void startDataParses()
    {
        olxParser = new OlxParser();
        asyncTasks = new ArrayList<>();
        AsyncGetAll parsingAyncTask;
        for(int i = 2; i < gameSize; i++)
        {
            parsingAyncTask = new AsyncGetAll(this, app, i, olxParser);
            parsingAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            asyncTasks.add(parsingAyncTask);
        }
    }

    @Override
    public void onBackPressed()
    {
        if(slideupGuesser.getState() == BottomSheetBehavior.STATE_EXPANDED)
            slideupGuesser.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
        {
            AlertDialog.Builder popup;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                popup = new AlertDialog.Builder(this, R.style.DialogTheme);
            else
                popup = new AlertDialog.Builder(this);
            popup.setTitle(getString(R.string.leavegame));
            popup.setMessage(getString(R.string.leavegamesub));
            popup.setCancelable(true);

            popup.setPositiveButton(
                    R.string.leave,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            leaveGame();
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

    private void leaveGame()
    {
        for(AsyncGetAll asyncTask : asyncTasks)
            asyncTask.cancel(true);

        finish();
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
