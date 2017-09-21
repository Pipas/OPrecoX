package software.pipas.oprecox.activities.multiPlayer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerLoader;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.interfaces.OnPlayerLoader;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.network.RoomService;

import software.pipas.oprecox.modules.parsing.AsyncGetAd;
import software.pipas.oprecox.modules.parsing.AsyncGetUrl;
import software.pipas.oprecox.modules.parsing.OlxParser;
import software.pipas.oprecox.util.Settings;

import software.pipas.oprecox.util.Util;

public class LobbyHost extends MultiplayerClass implements OnPlayerLoader, ParsingCallingActivity
{
    private final int OPTIONS_REQUEST_CODE = 1;

    private BroadcastReceiver broadcastReceiver;

    private Intent room;
    private String roomName;
    private Player player;
    private Boolean blocked;

    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;
    private ProgressDialog barDialog;
    private ProgressDialog circleDialog;
    private OlxParser olxParser;

    private ArrayList<String> urls;
    private OPrecoX app;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_host);
        this.startRoom();

        ListView listView = (ListView) findViewById(R.id.playersListViewer);
        this.players = new ArrayList<>();

        app = (OPrecoX) getApplicationContext();

        initiateCustomFonts();

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startGame();
            }
        });

        playerListAdapter = new PlayerListAdapter(this.players, getApplicationContext(), getContentResolver());
        listView.setAdapter(playerListAdapter);

        this.startBroadcastReceiver();
    }

    private void initiateCustomFonts()
    {
        TextView roomName = (TextView)findViewById(R.id.roomName);
        TextView roomHostName = (TextView)findViewById(R.id.roomHostName);
        TextView optionsTooltip = (TextView) findViewById(R.id.optionsTooltip);
        TextView inviteTooltip = (TextView) findViewById(R.id.inviteTooltip);

        CustomFontHelper.setCustomFont(roomName, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(roomHostName, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(optionsTooltip, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(inviteTooltip, "font/antipastopro-demibold.otf", getBaseContext());
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        super.onConnected(bundle);
        this.player = Games.Players.getCurrentPlayer(this.mGoogleApiClient);

        //load room image
        ImageView imageView = (ImageView) findViewById(R.id.roomImage);
        if(imageView.getDrawable() == null)
        {
            ImageManager imageManager = ImageManager.create(this);
            imageManager.loadImage(imageView, this.player.getHiResImageUri());
        }

        //load host name
        TextView displayName = (TextView) findViewById(R.id.roomHostName);
        displayName.setText(this.player.getDisplayName());

        //load player default room name
        String strTemp = this.player.getDisplayName() + "-Room";
        TextView roomNameView = (TextView) findViewById(R.id.roomName);

        if(roomNameView.getText().length() == 0)
        {
            this.roomName = strTemp;
            roomNameView.setText(this.roomName);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == OPTIONS_REQUEST_CODE && resultCode == RESULT_OK)
        {
            String newRoomName = intent.getExtras().getString(getResources().getString(R.string.roomName));
            this.roomName = newRoomName;
            TextView roomName = (TextView) findViewById(R.id.roomName);
            roomName.setText(this.roomName);

            Intent newIntent = new Intent(getString(R.string.S004));
            newIntent.putExtra(getString(R.string.S004_ACTUALIZEROOMNAME), Util.substituteSpace(this.roomName));
            sendBroadcast(newIntent);
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(this.room != null) stopService(this.room);
        if(this.broadcastReceiver != null) unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void startGame()
    {
        //CHECK IF IT HAS PEOPLE IN THE ROOM
        if(players.size() <= 0)
        {
            Toast.makeText(this.getApplicationContext(), getString(R.string.aloneinlobby), Toast.LENGTH_SHORT).show();
            return;
        }

        //STARTS LOADING THE ADS
        olxParser = new OlxParser();
        urls = new ArrayList<>();
        AsyncGetUrl parsingAsyncTask;
        startProgressBar();
        for(int i = 0; i < Settings.getGameSize(); i++)
        {
            parsingAsyncTask = new AsyncGetUrl(this, olxParser);
            parsingAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void onInvitePressed(View v)
    {
        Intent intent = new Intent(this, Invite.class);
        intent.putExtra(getResources().getString(R.string.roomName), this.roomName);
        startActivity(intent);
    }

    public void onOptionsPressed(View v)
    {
        Intent intent = new Intent(this, Options.class);
        intent.putExtra(getResources().getString(R.string.roomName), this.roomName);
        startActivityForResult(intent, OPTIONS_REQUEST_CODE);
    }

    private void startRoom()
    {
        this.room = new Intent(this, RoomService.class);
        startService(this.room);
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleReceivedIntent(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getString(R.string.S007));
        registerReceiver(this.broadcastReceiver, filter);
    }

    //for receive of intents S007
    private void handleReceivedIntent(Context context, Intent intent)
    {
        Log.d("LOBBY_HOST", intent.toString());
        software.pipas.oprecox.modules.dataType.Player player = intent.getExtras().getParcelable(getString(R.string.S007_ADDPLAYERLIST));

        if(player != null)
        {
            this.players.add(player);
            this.refreshListAdapter(this.playerListAdapter);
            return;
        }

        player = intent.getExtras().getParcelable(getString(R.string.S007_REMOVEPLAYERLIST));
        if(player != null)
        {
            this.players.remove(player);
            this.refreshListAdapter(this.playerListAdapter);
            return;
        }

        player = intent.getExtras().getParcelable(getString(R.string.S007_REQUESPLAYERLOADER));
        if (player != null)
        {
            Log.d("MY_IP_DEBUG", "sendload->load");

            PlayerLoader playerLoader = new PlayerLoader(LobbyHost.this, this.mGoogleApiClient, player);
            playerLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            Log.d("MY_IP_DEBUG", "trying to load");
            return;
        }
    }

    private void refreshListAdapter(PlayerListAdapter playerListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(playerListAdapter, null, null);
        this.runOnUiThread(listAdapterRefresh);
    }

    @Override
    public void playerLoaded(software.pipas.oprecox.modules.dataType.Player player)
    {
        Log.d("MY_IP_DEBUG","loaded" + "\n" + player.toString());
        Intent intent = new Intent(getString(R.string.S004));
        intent.putExtra(getString(R.string.S004_LOADEDPLAYER), player);
        sendBroadcast(intent);
    }

    public void addAdUrl(String url)
    {
        if(urls == null)
            urls = new ArrayList<>();
        urls.add(url);

        checkUrlParsingEnd();
    }

    private void checkUrlParsingEnd()
    {
        if(urls.size() == Settings.getGameSize())
        {
            barDialog.dismiss();
            sendUrlsToPlayers();
        }
        else
            barDialog.setProgress(urls.size());
    }

    private void sendUrlsToPlayers()
    {
        //SEND URLS TO OTHER PLAYERS

        //LOAD 2 ADS
        app.setAds(new Ad [Settings.getGameSize()]);
        AsyncGetAd parsingAsyncTask;
        startProgressCircle();
        for(int i = 0; i < 2; i++)
        {
            parsingAsyncTask = new AsyncGetAd(this, app, urls.get(i), i, olxParser);
            parsingAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void startProgressBar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            barDialog = new ProgressDialog(this, R.style.DialogThemePurple);
        else
            barDialog = new ProgressDialog(this);
        barDialog.setMessage("A carregar anúncios");
        barDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barDialog.setProgress(0);
        barDialog.setMax(Settings.getGameSize());
        barDialog.setCancelable(false);
        barDialog.show();
    }

    private void startProgressCircle()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            circleDialog = new ProgressDialog(this, R.style.DialogThemePurple);
        else
            circleDialog = new ProgressDialog(this);
        circleDialog.setIndeterminate(true);
        circleDialog.setMessage("A carregar anuncios");
        circleDialog.setCancelable(false);
        circleDialog.show();
    }

    @Override
    public void parsingEnded()
    {
        if(app.getAds()[0] != null && app.getAds()[0] != null)
            circleDialog.dismiss();
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
}
