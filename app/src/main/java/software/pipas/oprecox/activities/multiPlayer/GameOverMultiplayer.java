package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Arrays;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.AdOverviewAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.modules.database.DatabaseHandler;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;
import software.pipas.oprecox.util.Settings;

public class GameOverMultiplayer extends AppCompatActivity
{
    private AdOverviewAdapter adOverviewAdapter;
    private OPrecoX app;
    private int score;
    private ListView adOverviewList;
    private TextView scoreOutput;
    private DatabaseHandler database;
    private ArrayList<Integer> savedAds = new ArrayList<>();

    private InterstitialAd mInterstitialAd;
    private Boolean playAd;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        app = (OPrecoX) getApplicationContext();
        if(app.getAds() == null)
        {
            finish();
            return;
        }

        Settings.increaseGamesPlayed(this);
        playAd = (Settings.getAdCountdown() == 0);
        if(playAd)
        {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-9386790266312341/4116148417");
            AdRequest ad = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(ad);
            mInterstitialAd.setAdListener(new AdListener()
            {
                @Override
                public void onAdClosed()
                {
                    finish();
                }
            });
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        database = new DatabaseHandler(this, GameOverMultiplayer.this);

        scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        TextView finalScoreTextView = (TextView) findViewById(R.id.finalScoreTextView);
        TextView gameOverTitle = (TextView) findViewById(R.id.gameOverTitle);

        adOverviewList = (ListView) findViewById(R.id.adOverviewList);

        CustomFontHelper.setCustomFont(scoreOutput, "font/Comfortaa_Thin.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(finalScoreTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameOverTitle, "font/antipastopro-demibold.otf", getBaseContext());

        initiateViews();

        this.startBroadcastReceiver();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(this.broadcastReceiver);
    }

    private void initiateViews()
    {
        ArrayList<Ad> ads = new ArrayList<>(Arrays.asList(app.getAds()));
        adOverviewAdapter = new AdOverviewAdapter(ads, getApplicationContext(), getContentResolver());
        adOverviewList.setAdapter(adOverviewAdapter);
        adOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                saveAd(position);
            }
        });

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pressFinish();
            }
        });

        scoreOutput.setText(String.format("%d", score));
    }

    private void saveAd(int index)
    {
        if(!savedAds.contains(index))
        {
            database.open();
            ArrayList<AdPreview> previews = database.getAllAds();
            for(AdPreview preview : previews)
            {
                if(preview.getTitle().equals(app.getAd(index).getTitle()))
                {
                    database.close();
                    savedAds.add(index);
                    return;
                }
            }
            database.createAd(app.getAd(index));
            database.close();
            Toast.makeText(getBaseContext(), getResources().getString(R.string.savedAd), Toast.LENGTH_SHORT).show();
            savedAds.add(index);
        }
    }

    private void pressFinish()
    {
        resetAdArray();
        Log.d("Ads", "AdCountdown = " + Settings.getAdCountdown());
        if(playAd)
        {
            if (mInterstitialAd.isLoaded())
            {
                Settings.updateAdCountdown(this);
                mInterstitialAd.show();
            }
            else
                finish();
        }
        else
        {
            Settings.updateAdCountdown(this);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        pressFinish();
    }

    public void resetAdArray()
    {
        app.setAds(null);
    }

    private void startBroadcastReceiver()
    {
        IntentFilter filter = new IntentFilter(getString(R.string.S009));
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleIntentReceived(context, intent);
            }
        };

        this.registerReceiver(this.broadcastReceiver, filter);
    }

    private void handleIntentReceived(Context context, Intent intent)
    {
        String response = intent.getExtras().getString(getResources().getString(R.string.S009_RESPONSE));
        if (response != null && response.equals(ResponseType.CLOSED.toString()))
        {
            finish();
            return;
        }

        String message = intent.getExtras().getString(getString(R.string.S008_MESSAGE));
        if(message != null)
        {
            Message msg = new Message(this.getApplicationContext(), message);


            if (msg.isValid() && msg.getMessageType().equals(MessageType.EXITGAMEACTIVITY.toString())) {
                finish();
            }
        }
    }
}
