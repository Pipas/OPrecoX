package software.pipas.oprecox.activities.multiPlayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.singlePlayer.GameActivity;
import software.pipas.oprecox.activities.singlePlayer.GameOver;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.database.DatabaseHandler;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;
import software.pipas.oprecox.modules.parsing.AsyncGetAd;
import software.pipas.oprecox.modules.parsing.OlxParser;

public class PriceGuessGameMultiplayerActivity extends GameActivity implements ParsingCallingActivity
{
    private View afterGuess;
    private View priceGuesser;
    private View waitingView;

    private ArrayList<TextView> dialpadButtons = new ArrayList<>();
    private TextView beforePriceGuess;
    private TextView afterPriceGuess;
    private TextView waitingPriceGuess;
    private TextView scorePlusTextView;
    private TextView correctPriceOutput;
    private TextView scoreOutput;

    private String dialpadString = "";
    private float guessPrice;

    private int scorePlus;

    private DatabaseHandler database;
    private Boolean adSaved = false;
    private Boolean isHost;
    private String playerId;

    private BroadcastReceiver broadcastReceiver;
    private ArrayList<AsyncGetAd> asyncTasks;
    private ArrayList<String> urls;

    @Override
    public void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);

        database = new DatabaseHandler(this, PriceGuessGameMultiplayerActivity.this);

        gameSize = getIntent().getIntExtra(getString(R.string.S008_GAMESIZE), 10);

        urls = (ArrayList<String>) getIntent().getSerializableExtra(getString(R.string.S008_GAMEURLS));

        isHost = getIntent().getBooleanExtra(getString(R.string.S008_HOSTINACTIVITY), false);

        playerId = getIntent().getExtras().getString(getString(R.string.S008_PLAYERID));

        inflateGuesserViews();

        initiateCustomFonts();

        initiateButtonListeners();

        this.startBroadcastReceiver();

        startDataParses();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    protected void startDataParses()
    {
        Log.d("HERE", urls.size() + " " + gameSize);
        olxParser = new OlxParser();
        asyncTasks = new ArrayList<>();
        AsyncGetAd parsingAyncTask;
        for(int i = 2; i < gameSize; i++)
        {
            parsingAyncTask = new AsyncGetAd(this, app, urls.get(i), i, olxParser);
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
                            leaveGame(true);
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


    public static int getAdIndex()
    {
        return adIndex;
    }

    private void inflateGuesserViews()
    {
        FrameLayout guesserFrameLayout = (FrameLayout)findViewById(R.id.guesserFrameLayout);
        priceGuesser = getLayoutInflater().inflate(R.layout.price_guesser_layout, null);
        guesserFrameLayout.addView(priceGuesser);

        afterGuess = getLayoutInflater().inflate(R.layout.after_guess_layout, null);
        guesserFrameLayout.addView(afterGuess);
        afterGuess.setVisibility(View.GONE);

        waitingView = getLayoutInflater().inflate(R.layout.game_wait_layout, null);
        guesserFrameLayout.addView(waitingView);
        waitingView.setVisibility(View.GONE);

        LinearLayout continueButton = (LinearLayout) afterGuess.findViewById(R.id.continueButton);
        if(!isHost)
            continueButton.setVisibility(View.GONE);
    }

    private void initiateCustomFonts()
    {
        for(int i = 0; i < 10; i++)
        {
            String buttonID = "dialpad" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            TextView diapladButton = ((TextView) findViewById(resID));
            dialpadButtons.add(diapladButton);
        }

        TextView dialpadDot = (TextView)findViewById(R.id.dialpadDot);
        dialpadButtons.add(dialpadDot);

        beforePriceGuess = (TextView) priceGuesser.findViewById(R.id.priceGuess);
        TextView confirmButtonTextView = (TextView)findViewById(R.id.confirmButtonTextView);

        CustomFontHelper.setCustomFont(beforePriceGuess, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(confirmButtonTextView, "font/Antonio-Regular.ttf", getBaseContext());

        for(TextView dialpadButton : dialpadButtons)
        {
            CustomFontHelper.setCustomFont(dialpadButton, "font/Antonio-Regular.ttf", getBaseContext());
        }

        afterPriceGuess = (TextView) afterGuess.findViewById(R.id.priceGuess);
        TextView correctPriceOutputTextView = (TextView) afterGuess.findViewById(R.id.correctPriceOutputTextView);
        correctPriceOutput = (TextView) afterGuess.findViewById(R.id.correctPriceOutput);

        TextView scoreOutputTextView = (TextView) afterGuess.findViewById(R.id.scoreOutputTextView);
        scoreOutput = (TextView) afterGuess.findViewById(R.id.scoreOutput);
        scorePlusTextView = (TextView) afterGuess.findViewById(R.id.scorePlus);
        TextView continueButtonTextView = (TextView) findViewById(R.id.continueButtonTextView);

        waitingPriceGuess = (TextView) waitingView.findViewById(R.id.priceGuess);

        CustomFontHelper.setCustomFont(afterPriceGuess, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(correctPriceOutputTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(correctPriceOutput, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(scoreOutputTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(scoreOutput, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(scorePlusTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(continueButtonTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(waitingPriceGuess, "font/Antonio-Regular.ttf", getBaseContext());
    }

    private void initiateButtonListeners()
    {
        for(int i = 0; i < 11; i++)
        {
            final int tempIndex = i;
            dialpadButtons.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    updateNumber(tempIndex);
                }
            });
        }

        ImageView dialpadBackspace = (ImageView) priceGuesser.findViewById(R.id.dialpadBackspace);
        dialpadBackspace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateNumber(-1);
            }
        });

        ImageView savedAdsButton = (ImageView) afterGuess.findViewById(R.id.savedAdsButton);
        savedAdsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveAd();
            }
        });

        LinearLayout confirmButton = (LinearLayout) priceGuesser.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                confirmSelection();
            }
        });

        if(isHost)
        {
            LinearLayout continueButton = (LinearLayout) afterGuess.findViewById(R.id.continueButton);
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continuePressed();
                }
            });
        }
    }

    private void updateNumber(int digit)
    {
        String outputFormat;

        if(digit == -1)
        {
            if (dialpadString != null && dialpadString.length() > 0)
            {
                dialpadString = dialpadString.substring(0, dialpadString.length()-1);
            }
            else
                return;
        }
        else if(dialpadString.length() >= 9 || (dialpadString.contains(".") && dialpadString.substring(dialpadString.lastIndexOf(".") + 1).length() >= 2))
            return;


        if(digit == 0)
        {
            if (dialpadString.equals("0"))
                return;

            dialpadString += digit;
        }
        if(digit > 0 && digit < 10)
        {
            dialpadString += digit;
        }
        else if(digit == 10)
        {
            if(dialpadString.contains("."))
                return;
            else if(dialpadString.length() >= 5 || dialpadString.length() == 0)
                return;
            else
                dialpadString += ".";
        }

        if (dialpadString != null && dialpadString.length() > 0)
        {
            guessPrice = Float.parseFloat(dialpadString);
            if(guessPrice == (int) guessPrice)
                outputFormat = String.format("%,d", (int) guessPrice);
            else
                outputFormat = String.format("%,.2f", guessPrice);

            if(dialpadString.substring(dialpadString.length() - 1).equals("."))
            {
                outputFormat += ".";
            }

            outputFormat += "€";
        }
        else
            outputFormat = "";

        beforePriceGuess.setText(outputFormat);

        Log.d("INPUT", String.format("%s | %s", dialpadString, beforePriceGuess.getText()));
    }

    private void leaveGame(boolean forceExit)
    {
        for(AsyncGetAd asyncGetAd : this.asyncTasks)
        {
            asyncGetAd.cancel(true);
        }

        if (forceExit) setActivityResult();

        finish();
    }

    private void setActivityResult()
    {
        if(!isHost)
        {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.S006_CLIENTFORCEEXITGAME), "");
            setResult(RESULT_OK, intent);
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.S007_HOSTFORCEEXITGAME), "");
            setResult(RESULT_OK, intent);
        }
    }

    private void calculateScore()
    {
        float correctPrice = app.getAd(adIndex).getPrice();

        if(correctPrice == guessPrice)
            scorePlus = 50;
        else if(correctPrice <= 30)
        {
            if(guessPrice >= 0.75*correctPrice && guessPrice <= 1.25*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.5*correctPrice && guessPrice <= 1.5*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
            }
        }
        else if(correctPrice <= 80)
        {
            if(guessPrice >= 0.85*correctPrice && guessPrice <= 1.15*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.6*correctPrice && guessPrice <= 1.4*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
            }
        }
        else if(correctPrice <= 10000)
        {
            if(guessPrice >= 0.90*correctPrice && guessPrice <= 1.10*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.75*correctPrice && guessPrice <= 1.25*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
            }
        }
        else
        {
            if(guessPrice >= 0.90*correctPrice && guessPrice <= 1.10*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.85*correctPrice && guessPrice <= 1.15*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
            }
        }

    }

    private void confirmSelection()
    {
        if(dialpadString.equals(""))
            return;

        priceGuesser.setVisibility(View.GONE);
        if(guessPrice == (int) guessPrice)
            waitingPriceGuess.setText(String.format("%,d€", (int) guessPrice));
        else
            waitingPriceGuess.setText(String.format("%,.2f€", guessPrice));
        waitingView.setVisibility(View.VISIBLE);

        calculateScore();
        sendRoundAnswer(guessPrice, scorePlus);
    }

    private void showAfterGuessView()
    {
        waitingView.setVisibility(View.GONE);
        afterGuess.setVisibility(View.VISIBLE);
        populateAfterGuessViews();
    }

    private void continuePressed()
    {
        if(adIndex >= gameSize - 1)
        {
            Intent myIntent = new Intent(this, GameOver.class);
            myIntent.putExtra("score", score);
            startActivity(myIntent);
            finish();
        }
        else
        {
            showBeforeGuessView();

            Intent intent = new Intent(getString(R.string.S004));
            intent.putExtra(getString(R.string.S004_NEXTROUND), "");
            sendBroadcast(intent);
        }
    }

    private void showBeforeGuessView()
    {
        //FAZER UMA THREAD WHILE NOT LOADED if(app.getAd(adIndex + 1) == null)
        adIndex++;

        setViewsWithAd(app.getAd(adIndex));
        dialpadString = "";
        beforePriceGuess.setText("");
        scorePlusTextView.setVisibility(View.GONE);
        togglePanel(null);
        
        priceGuesser.setVisibility(View.VISIBLE);
        afterGuess.setVisibility(View.GONE);
    }

    private void saveAd()
    {
        if(!adSaved)
        {
            database.open();
            database.createAd(app.getAd(adIndex));
            database.close();
            adSaved = true;
            Toast.makeText(getBaseContext(), getResources().getString(R.string.savedAd), Toast.LENGTH_SHORT).show();
        }
    }

    private void populateAfterGuessViews()
    {
        float correctPrice = app.getAd(adIndex).getPrice();

        if(guessPrice == (int) guessPrice)
            afterPriceGuess.setText(String.format("%,d€", (int) guessPrice));
        else
            afterPriceGuess.setText(String.format("%,.2f€", guessPrice));


        if(correctPrice == (int) correctPrice)
            correctPriceOutput.setText(String.format("%,d€", (int) correctPrice));
        else
            correctPriceOutput.setText(String.format("%,.2f€", correctPrice));

        scoreOutput.setText(String.format("%d", score));

        if(scorePlus != 0)
        {
            scorePlusTextView.setText(String.format("+%d", scorePlus));
            scorePlusTextView.setVisibility(View.VISIBLE);
            afterPriceGuess.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.correctGreen));
            score += scorePlus;
            app.getAd(adIndex).setScoreGained(scorePlus);
        }
        else
            afterPriceGuess.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.wrongRed));
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleIntentReceived(context, intent);
            }
        };

        IntentFilter filter = new IntentFilter(getString(R.string.S008));
        registerReceiver(this.broadcastReceiver, filter);

    }

    private void handleIntentReceived(Context context, Intent intent)
    {
        String response = intent.getExtras().getString(getResources().getString(R.string.S008_RESPONSE));
        if (response != null && response.equals(ResponseType.CLOSED.toString()))
        {
            finish();
            return;
        }


        String message = intent.getExtras().getString(getString(R.string.S008_MESSAGE));
        if(message != null)
        {
            Message msg = new Message(this.getApplicationContext(), message);

            if(msg.isValid() && msg.getMessageType().equals(MessageType.EXITGAMEACTIVITY.toString()))
            {
                finish();
            }
            else if(msg.isValid() && msg.getMessageType().equals(MessageType.CONTINUEGAME.toString()))
            {
                showAfterGuessView();
            }
            else if(msg.isValid() && msg.getMessageType().equals(MessageType.NEXTROUND.toString()) && !isHost)
            {
                showBeforeGuessView();
            }
        }
    }

    private void sendRoundAnswer(float answer, int score)
    {

        String[] args = new String[7];

        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.ROUNDSCORE.toString();
        args[3] = playerId;
        args[4] = Integer.toString(adIndex);
        args[5] = Float.toString(answer);
        args[6] = Integer.toString(score);

        Message msg = new Message(this.getApplicationContext(), args);

        if (!msg.isValid()) {
            Log.d("CLIENT_DEBUG", "round score message invalid");
            return;
        }

        if(!isHost)
        {
            Intent intent = new Intent(getString(R.string.S005));
            intent.putExtra(getString(R.string.S005_MESSAGE), msg.getMessage());
            sendBroadcast(intent);
        }
        else
        {
            Intent intent = new Intent(getString(R.string.S004));
            intent.putExtra(getString(R.string.S004_ROUNDANSWER), msg.getMessage());
            sendBroadcast(intent);
        }
    }
}
