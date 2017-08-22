package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.database.DatabaseHandler;
import software.pipas.oprecox.modules.fragments.GameDataFragment;
import software.pipas.oprecox.modules.imageViewer.ImageViewer;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;
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
    private Ad shownAd;
    private ArrayList<Ad> ads = new ArrayList<Ad>();

    private SlidingUpPanelLayout slider;
    private ProgressDialog mProgressDialog;

    private DatabaseHandler database;
    private Boolean adSaved = false;
    private Boolean guessMade = false;

    private GameDataFragment gameDataFragment;


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

        database = new DatabaseHandler(this);

        addArrowSliderListener();

        FragmentManager fm = getFragmentManager();
        gameDataFragment = (GameDataFragment) fm.findFragmentByTag("gamedata");
        if (gameDataFragment == null)
        {
            gameDataFragment = new GameDataFragment();
            fm.beginTransaction().add(gameDataFragment, "gamedata").commit();

            startProcessDialog();

            startDataParses(intent);
        }
        else
        {
            NGUESSES = gameDataFragment.getNGUESSES();
            score = gameDataFragment.getScore();
            correctGuesses = gameDataFragment.getCorrectGuesses();
            ads = gameDataFragment.getAds();
            shownAd = gameDataFragment.getShownAd();
            guess = gameDataFragment.getGuess();
            setViewsWithAd();
            slider.setVisibility(View.VISIBLE);
            if(guess != -1f)
            {
                toggleBeforeGuessLayout();

                toggleAfterGuessLayout();

                TextView priceGuess = (TextView) findViewById(R.id.priceGuess);
                String guessString = (int) guess + "€";
                priceGuess.setText(guessString);

                if (updateScore(guess))
                    priceGuess.setTextColor(Color.parseColor("#4caf50"));
                else
                    priceGuess.setTextColor(Color.parseColor("#f44336"));

                TextView correctPriceOutput = (TextView) findViewById(R.id.correctPriceOutput);
                if (shownAd.getPrice() == (int) shownAd.getPrice())
                    correctPriceOutput.setText(String.format("%d€", (int) shownAd.getPrice()));
                else
                    correctPriceOutput.setText(String.format("%.2f€", shownAd.getPrice()));

                guessMade = true;
            }
        }
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
    public void onDestroy()
    {
        Log.d("DEBUG", "Im being destroyed");
        super.onDestroy();
        if(guessMade)
            gameDataFragment.setData(NGUESSES, score, correctGuesses, ads, shownAd, guess);
        else
            gameDataFragment.setData(NGUESSES, score, correctGuesses, ads, shownAd, -1f);
    }

    public void setViewsWithAd()
    {
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(shownAd.getTitle());

        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        descriptionTextView.setText(shownAd.getDescription());

        adSaved = false;
    }


    public void setShownAd(Ad a)
    {
        shownAd = a;
    }

    public void addAdd(Ad a)
    {
        ads.add(a);
    }

    public void closeProgressPopup()
    {
        mProgressDialog.dismiss();
        if(Settings.isLocked())
            Util.lockApp(this);

        setViewsWithAd();
        slider.setVisibility(View.VISIBLE);
    }

    public boolean updateShownAdd()
    {
        shownAd = ads.get(0);
        ads.remove(0);
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

        TextView priceGuess = (TextView) findViewById(R.id.priceGuess);
        String guessString = dialpadNumber + "€";
        priceGuess.setText(guessString);

        if (updateScore(guess)) {
            priceGuess.setTextColor(Color.parseColor("#4caf50"));
        } else
            priceGuess.setTextColor(Color.parseColor("#f44336"));

        TextView correctPriceOutput = (TextView) findViewById(R.id.correctPriceOutput);
        if (shownAd.getPrice() == (int) shownAd.getPrice())
            correctPriceOutput.setText(String.format("%d€", (int) shownAd.getPrice()));
        else
            correctPriceOutput.setText(String.format("%.2f€", shownAd.getPrice()));

        guessMade = true;
    }

    private boolean updateScore(float guess)
    {

        if(guess == shownAd.getPrice())
        {
            correctGuess(50);
        }
        else if(guess >= (shownAd.getPrice() - shownAd.getPrice() * 0.05) && guess <= (shownAd.getPrice() + shownAd.getPrice() * 0.05))
        {
            correctGuess(25);
        }
        else if(guess >= (shownAd.getPrice() - shownAd.getPrice() * 0.2) && guess <= (shownAd.getPrice() + shownAd.getPrice() * 0.2))
        {
            correctGuess(10);
        }
        else if(guess >= (shownAd.getPrice() - shownAd.getPrice() * 0.5) && guess <= (shownAd.getPrice() + shownAd.getPrice() * 0.5))
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
        if(!adSaved)
        {
            database.open();
            database.createAd(shownAd);
            database.close();
            adSaved = true;
        }
        Toast.makeText(this, "Anúncio Guardado", Toast.LENGTH_SHORT).show();
    }

    public void pressContinueButton(View v)
    {
        if(!ads.isEmpty())
        {
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.fullScroll(View.FOCUS_UP);

            if(!updateShownAdd())
            {
                Toast.makeText(this,"acarregar", Toast.LENGTH_SHORT).show();
                return;
            }
            setViewsWithAd();
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

        guessMade = false;
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
        myIntent.putExtra("page", page);
        OPrecoX app = (OPrecoX) getApplicationContext();
        startActivityForResult(myIntent, 1);
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
        mProgressDialog.setTitle("ASDF");
        mProgressDialog.setMessage("LADSF");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void startDataParses(Intent intent)
    {
        OPrecoX app = (OPrecoX) getApplicationContext();
        AsyncGetAll firstParse = new AsyncGetAll(this, app, 0);
        AsyncGetAll backgroundParse;
        firstParse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        for (int i = 1; i < NGUESSES; i++)
        {
            backgroundParse = new AsyncGetAll(this, app, i);
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

    @Override
    public void parsingEnded()
    {

    }
}
