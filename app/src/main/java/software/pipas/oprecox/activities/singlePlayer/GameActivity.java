package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.app.AlertDialog;
import android.widget.Toast;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.modules.add.Add;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;
import software.pipas.oprecox.modules.imageViewer.ImagePagerAdapter;
import software.pipas.oprecox.modules.imageViewer.ImageViewer;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import software.pipas.oprecox.activities.menus.MainMenu;
import software.pipas.oprecox.R;
import software.pipas.oprecox.util.Settings;
import software.pipas.oprecox.util.Util;

public class GameActivity extends AppCompatActivity implements ParsingCallingActivity
{
    private int NGUESSES;
    private int score = 0;
    private int correctGuesses = 0;

    private TextView dialpadOutput;
    private float guess;
    private String dialpadNumber = "";
    private Add shownAdd;
    private ArrayList<Add> adds = new ArrayList<Add>();

    private SlidingUpPanelLayout slider;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        NGUESSES = intent.getIntExtra("NGUESSES", 10);

        dialpadOutput = (TextView) findViewById(R.id.dialpadNumber);
        dialpadOutput.setText(dialpadNumber);

        addArrowSliderListener();

        addViewPagerListener();

        startProcessDialog();

        startDataParses(intent);
    }

    @Override
    public void onBackPressed()
    {
        com.sothree.slidinguppanel.SlidingUpPanelLayout slidingPanel = (com.sothree.slidinguppanel.SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if(slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
        {
            AlertDialog.Builder popup = new AlertDialog.Builder(this);
            popup.setTitle(getString(R.string.leavegame));
            popup.setMessage(getString(R.string.leavegamesub));
            popup.setCancelable(true);

            popup.setPositiveButton(
                    R.string.leave,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent myIntent = new Intent(GameActivity.this, MainMenu.class);
                            startActivity(myIntent);
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

    public void setViewsWithAdd()
    {
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        ImagePagerAdapter adapterViewPager = new ImagePagerAdapter(getSupportFragmentManager(), shownAdd.getBmImages());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        if(shownAdd.getImages().size() == 1)
            indicator.setVisibility(View.GONE);
        else
            indicator.setVisibility(View.VISIBLE);
        indicator.setViewPager(vpPager);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(shownAdd.getTitle());

        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        descriptionTextView.setText(shownAdd.getDescription());
    }


    public void setShownAdd(Add a)
    {
        shownAdd = a;
    }

    public void addAdd(Add a)
    {
        adds.add(a);
    }

    public void closeProgressPopup()
    {
        mProgressDialog.dismiss();
        if(Settings.isLocked())
            Util.lockApp(this);

        setViewsWithAdd();
        slider.setVisibility(View.VISIBLE);
    }

    public boolean updateShownAdd()
    {
        shownAdd = adds.get(0);
        adds.remove(0);
        return true;
    }

    public void pressButton1(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "1";
        updateNumber();
    }

    public void pressButton2(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "2";
        updateNumber();
    }

    public void pressButton3(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "3";
        updateNumber();
    }

    public void pressButton4(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "4";
        updateNumber();
    }

    public void pressButton5(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "5";
        updateNumber();
    }

    public void pressButton6(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "6";
        updateNumber();
    }

    public void pressButton7(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "7";
        updateNumber();
    }

    public void pressButton8(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "8";
        updateNumber();
    }

    public void pressButton9(View v)
    {
        if(dialpadNumber.length() > 6)
            return;
        dialpadNumber += "9";
        updateNumber();
    }

    public void pressButton0(View v)
    {
        if (dialpadNumber.length() > 6 || dialpadNumber.equals("0"))
            return;
        dialpadNumber += "0";
        updateNumber();
    }

    public void pressButtonDot(View v)
    {
        if(dialpadNumber.contains(".") || dialpadNumber.length() > 6 || dialpadNumber.length() == 0)
            return;
        dialpadNumber += ".";
        updateNumber();
    }

    public void pressButtonBackspace(View v)
    {
        if (dialpadNumber != null && dialpadNumber.length() > 0)
        {
            dialpadNumber = dialpadNumber.substring(0, dialpadNumber.length()-1);
        }
        updateNumber();
    }

    private void updateNumber()
    {
        if(dialpadNumber.isEmpty())
            dialpadOutput.setText(dialpadNumber);
        else
        {
            String num = dialpadNumber + "€";
            dialpadOutput.setText(num);
        }
    }

    public void pressConfirmButton (View v)
    {
        try
        {
            guess = Float.parseFloat(dialpadNumber);
            Log.d("INPUT", String.format("%f", guess));
        }
        catch (NumberFormatException e)
        {
            Log.d("INPUT", "Exception caught invalid input");
            return;
        }

        toggleBeforeGuessLayout();

        toggleAfterGuessLayout();

        //Set guess
        TextView priceGuess = (TextView) findViewById(R.id.priceGuess);
        String guessString = dialpadNumber + "€";
        priceGuess.setText(guessString);

        //Set color
        if (updateScore(guess)) {
            priceGuess.setTextColor(Color.parseColor("#4caf50"));
        } else
            priceGuess.setTextColor(Color.parseColor("#f44336"));

        //Prints without , if its equal to int
        TextView correctPriceOutput = (TextView) findViewById(R.id.correctPriceOutput);
        if (shownAdd.getPrice() == (int) shownAdd.getPrice())
            correctPriceOutput.setText(String.format("%d€", (int) shownAdd.getPrice()));
        else
            correctPriceOutput.setText(String.format("%.2f€", shownAdd.getPrice()));

    }

    private boolean updateScore(float guess)
    {

        if(guess == shownAdd.getPrice())
        {
            correctGuess(50);
        }
        else if(guess >= (shownAdd.getPrice() - shownAdd.getPrice() * 0.05) && guess <= (shownAdd.getPrice() + shownAdd.getPrice() * 0.05))
        {
            correctGuess(25);
        }
        else if(guess >= (shownAdd.getPrice() - shownAdd.getPrice() * 0.2) && guess <= (shownAdd.getPrice() + shownAdd.getPrice() * 0.2))
        {
            correctGuess(10);
        }
        else if(guess >= (shownAdd.getPrice() - shownAdd.getPrice() * 0.5) && guess <= (shownAdd.getPrice() + shownAdd.getPrice() * 0.5))
        {
            correctGuess(5);
        }
        else
            return false;

        return true;
    }

    private void correctGuess(int points)
    {
        TextView scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        TextView scorePlus = (TextView) findViewById(R.id.scorePlus);
        scorePlus.setVisibility(View.VISIBLE);
        scoreOutput.setText(Integer.toString(score));
        score += points;
        String plus = "+" + Integer.toString(points);
        scorePlus.setText(plus);
        correctGuesses++;
    }

    public void pressShare(View v)
    {
        Log.d("URL", shownAdd.getUrl());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shownAdd.getUrl()));
        startActivity(browserIntent);
    }

    public void pressContinueButton(View v)
    {
        if(!adds.isEmpty())
        {
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.fullScroll(View.FOCUS_UP);

            if(!updateShownAdd())
            {
                Toast.makeText(this,getString(R.string.loading), Toast.LENGTH_SHORT).show();
                return;
            }
            setViewsWithAdd();
        }
        else
        {
            Intent myIntent = new Intent(this, GameOver.class);
            myIntent.putExtra("score", score);
            myIntent.putExtra("correctGuesses", correctGuesses);
            myIntent.putExtra("NGUESSES", NGUESSES);
            startActivity(myIntent);
            finish();
            return;
        }

        resetDialpadNuber();

        //Collapse sliding panel
        com.sothree.slidinguppanel.SlidingUpPanelLayout slidingPanel = (com.sothree.slidinguppanel.SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        toggleAfterGuessLayout();

        toggleBeforeGuessLayout();

        //Hide + score and update score
        TextView scorePlus = (TextView) findViewById(R.id.scorePlus);
        TextView scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        if(scorePlus.getVisibility() == View.VISIBLE)
            scorePlus.setVisibility(View.GONE);
        scoreOutput.setText(Integer.toString(score));
    }

    private void resetDialpadNuber()
    {
        dialpadNumber = "";
        dialpadOutput.setText(dialpadNumber);
    }

    private void toggleBeforeGuessLayout()
    {
        LinearLayout dialpadLayout = (LinearLayout) findViewById(R.id.dialpadLayout);
        LinearLayout bottomOptions = (LinearLayout) findViewById(R.id.bottomOptions);
        RelativeLayout dialpadOutput = (RelativeLayout) findViewById(R.id.dialpadOutput);

        if(dialpadOutput.getVisibility() == View.VISIBLE)
            dialpadOutput.setVisibility(View.GONE);
        else
            dialpadOutput.setVisibility(View.VISIBLE);

        if (dialpadLayout.getVisibility() == View.VISIBLE)
            dialpadLayout.setVisibility(View.GONE);
        else
            dialpadLayout.setVisibility(View.VISIBLE);

        if(bottomOptions.getVisibility() == View.VISIBLE)
            bottomOptions.setVisibility(View.GONE);
        else
            bottomOptions.setVisibility(View.VISIBLE);
    }

    private void toggleAfterGuessLayout()
    {
        LinearLayout scoreLayout = (LinearLayout) findViewById(R.id.scoreLayout);
        LinearLayout bottomOptionsAfterGuess = (LinearLayout) findViewById(R.id.bottomOptionsAfterGuess);

        if (bottomOptionsAfterGuess.getVisibility() == View.VISIBLE)
            bottomOptionsAfterGuess.setVisibility(View.GONE);
        else
            bottomOptionsAfterGuess.setVisibility(View.VISIBLE);

        if(scoreLayout.getVisibility() == View.VISIBLE)
            scoreLayout.setVisibility(View.GONE);
        else
            scoreLayout.setVisibility(View.VISIBLE);
    }

    public void startImageViewerActivity(int page)
    {
        Intent myIntent = new Intent(this, ImageViewer.class);
        myIntent.putExtra("images", shownAdd.getImages());
        myIntent.putExtra("page", page);
        startActivityForResult(myIntent, 1);
    }

    private void addViewPagerListener()
    {
        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOnTouchListener(new OnTouchListener() {
            private float pointX;
            private float pointY;
            private int tolerance = 50;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        return false; //This is important, if you return TRUE the action of swipe will not take place.
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                        boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                        if(sameX && sameY)
                        {
                            startImageViewerActivity(vpPager.getCurrentItem());
                        }
                }
                return false;
            }
        });
    }

    private void addArrowSliderListener()
    {
        final ImageView arrow = (ImageView) findViewById(R.id.downarrow);

        slider = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slider.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener()
        {
            float prevSlideOffset = 0;

            @Override
            public void onPanelSlide(View panel, float slideOffset)
            {
                if(slideOffset - prevSlideOffset < 0 && arrow.getRotation() == 0) //Baixo
                    arrow.setRotation(180);

                if(slideOffset - prevSlideOffset > 0 && arrow.getRotation() == 180) //Cima
                    arrow.setRotation(0);

                prevSlideOffset = slideOffset;
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {}
        });
        slider.setVisibility(View.GONE);

        arrow.setRotation(180);
    }

    private void startProcessDialog()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.loadingadds);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void startDataParses(Intent intent)
    {
        AsyncGetAll firstParse = new AsyncGetAll(this, true);
        AsyncGetAll backgroundParse;
        firstParse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        for (int i = 1; i < NGUESSES; i++)
        {
            backgroundParse = new AsyncGetAll(this, false);
            backgroundParse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
                vpPager.setCurrentItem(page);
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }
}
