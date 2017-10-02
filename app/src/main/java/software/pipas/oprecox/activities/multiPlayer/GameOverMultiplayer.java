package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.MultiplayerOverviewAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;
import software.pipas.oprecox.util.Settings;

public class GameOverMultiplayer extends AppCompatActivity
{
    private MultiplayerOverviewAdapter multiplayerOverviewAdapter;
    private OPrecoX app;
    private HashMap<Player, Integer> scores;
    private ListView multiplayerOverviewList;

    private InterstitialAd mInterstitialAd;
    private Boolean playAd;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(Settings.getSharedPlayerDB() == null)
        {
            finish();
            return;
        }

        app = (OPrecoX) getApplicationContext();
        resetAdArray();

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
        setContentView(R.layout.activity_multiplayer_game_over);

        Intent intent = getIntent();
        scores = (HashMap<Player, Integer>) intent.getSerializableExtra("scores");


        TextView gameOverTitle = (TextView) findViewById(R.id.gameOverTitle);

        multiplayerOverviewList = (ListView) findViewById(R.id.adOverviewList);

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
        multiplayerOverviewAdapter = new MultiplayerOverviewAdapter(scores, getApplicationContext(), getContentResolver());
        multiplayerOverviewList.setAdapter(multiplayerOverviewAdapter);

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pressFinish();
            }
        });
    }


    private void pressFinish()
    {
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

        String message = intent.getExtras().getString(getString(R.string.S009_MESSAGE));
        if(message != null)
        {
            Message msg = new Message(this.getApplicationContext(), message);


            if (msg.isValid() && msg.getMessageType().equals(MessageType.EXITGAMEACTIVITY.toString())) {
                finish();
            }
            return;
        }

        String exitActivity = intent.getExtras().getString(getString(R.string.S009_EXITACTIVITY));
        if(exitActivity != null)
        {
            finish();
        }
    }
}
