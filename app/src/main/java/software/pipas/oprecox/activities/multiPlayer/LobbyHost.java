package software.pipas.oprecox.activities.multiPlayer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import es.dmoral.toasty.Toasty;
import software.pipas.oprecox.BuildConfig;
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
import software.pipas.oprecox.modules.interfaces.OnUrlLoaded;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.network.RoomService;
import software.pipas.oprecox.modules.parsing.AsyncGetAd;
import software.pipas.oprecox.modules.parsing.AsyncGetUrl;
import software.pipas.oprecox.modules.parsing.OlxParser;
import software.pipas.oprecox.util.Settings;
import software.pipas.oprecox.util.Util;

public class LobbyHost extends MultiplayerClass implements OnPlayerLoader, ParsingCallingActivity, OnUrlLoaded
{
    private final int OPTIONS_REQUEST_CODE = 1;
    private final int GAME_ACTIVITY_RESULT_CODE = 2;

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
    private ArrayList<AsyncGetUrl> parsingAsyncTasks = new ArrayList<>();

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
        if(Settings.getCustomName() == null)
            displayName.setText(this.player.getDisplayName());
        else
            displayName.setText(Settings.getCustomName());

        //load player default room name
        String strTemp = getString(R.string.newRoom);
        TextView roomNameView = (TextView) findViewById(R.id.roomName);

        if(roomNameView.getText().length() == 0)
        {
            this.roomName = strTemp;
            roomNameView.setText(this.roomName);
        }

        this.addMySelfToDB();
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
        else if(requestCode == GAME_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK)
        {

            String end = intent.getExtras().getString(getString(R.string.S007_HOSTFORCEEXITGAME));

            if(end != null)
            {
                String[] args = new String[3];

                args[0] = this.getString(R.string.network_app_name);
                args[1] = Integer.toString(BuildConfig.VERSION_CODE);
                args[2] = MessageType.EXITGAMEACTIVITY.toString();

                Message msg = new Message(this.getApplicationContext(), args);

                if (!msg.isValid()) {
                    Log.d("LOBBY_HOST_DEBUG", "EXITGAMEACTIVITY INVALID");
                    return;
                }

                Intent intent1 = new Intent(getString(R.string.S004));
                intent1.putExtra(getString(R.string.S004_EXITGAMEACTIVITY), msg.getMessage());
                sendBroadcast(intent1);
            }
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
            Toasty.info(this, getString(R.string.aloneinlobby), Toast.LENGTH_SHORT, true).show();
            return;
        }

        //SETS STUFF
        app.setAds(new Ad[Settings.getGameSize()]);

        //STARTS LOADING THE ADS
        olxParser = new OlxParser();
        urls = new ArrayList<>();
        startProgressBar();
        for(int i = 0; i < Settings.getGameSize(); i++)
        {
            AsyncGetUrl parsingAsyncTask = new AsyncGetUrl(LobbyHost.this, olxParser);
            parsingAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            parsingAsyncTasks.add(parsingAsyncTask);
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

    private void addMySelfToDB()
    {
        String realName = this.player.getName();
        if(realName == null) realName = this.player.getDisplayName();

        String displayName = Settings.getCustomName();
        if(displayName == null) displayName = this.player.getDisplayName();

        software.pipas.oprecox.modules.dataType.Player player = new software.pipas.oprecox.modules.dataType.Player(
                realName,
                displayName,
                this.player.getPlayerId(),
                this.player.getIconImageUri()
        );

        Settings.addToSharedPlayerDB(player);
    }

    //for receive of intents S007
    private void handleReceivedIntent(Context context, Intent intent)
    {
        Log.d("LOBBY_HOST", intent.toString());
        software.pipas.oprecox.modules.dataType.Player player = intent.getExtras().getParcelable(getString(R.string.S007_ADDPLAYERLIST));

        if(player != null)
        {
            Settings.addToSharedPlayerDB(player);
            this.checkAndAdd(player);
            this.refreshListAdapter(this.playerListAdapter);
            return;
        }

        player = intent.getExtras().getParcelable(getString(R.string.S007_REMOVEPLAYERLIST));
        if(player != null)
        {
            this.removePlayer(player);
            this.refreshListAdapter(this.playerListAdapter);
            return;
        }

        player = intent.getExtras().getParcelable(getString(R.string.S007_REQUESPLAYERLOADER));
        if (player != null)
        {
            PlayerLoader playerLoader = new PlayerLoader(LobbyHost.this, this.mGoogleApiClient, player);
            playerLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        String startGame = intent.getExtras().getString(getString(R.string.S007_STARTGAME));
        if(startGame != null)
        {
            circleDialog.dismiss();
            startGameActivity();
        }

    }

    private synchronized void checkAndAdd(software.pipas.oprecox.modules.dataType.Player player)
    {
        for(software.pipas.oprecox.modules.dataType.Player player1 : this.players)
        {
            if(player.equals(player1)) return;
        }

        this.players.add(player);
    }

    private synchronized void removePlayer(software.pipas.oprecox.modules.dataType.Player player)
    {
        this.players.remove(player);
    }

    private void refreshListAdapter(PlayerListAdapter playerListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(playerListAdapter, null, null);
        this.runOnUiThread(listAdapterRefresh);
    }

    @Override
    public void playerLoaded(software.pipas.oprecox.modules.dataType.Player player)
    {
        Intent intent = new Intent(getString(R.string.S004));
        intent.putExtra(getString(R.string.S004_LOADEDPLAYER), player);
        sendBroadcast(intent);
    }

    @Override
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
            if(players.size() <= 0)
            {
                Toasty.info(this, getString(R.string.aloneinlobby), Toast.LENGTH_SHORT, true).show();
                return;
            }
            sendUrlsToPlayers();
        }
        else
            barDialog.setProgress(urls.size());
    }

    private void sendUrlsToPlayers()
    {
        //SEND URLS TO OTHER PLAYERSurls
        int count = 3 + 1 + this.urls.size();
        String[] args = new String[count];

        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.GAMEURLS.toString();
        args[3] = Integer.toString(this.urls.size());

        for(int i = 4; i < count; i++)
        {
            args[i] = this.urls.get(i - 4);
        }

        Message msg = new Message(this.getApplicationContext(), args);

        if(msg.isValid())
        {
            Intent intent = new Intent(getString(R.string.S004));
            intent.putExtra(getString(R.string.S004_GAMEURLS), msg.getMessage());
            sendBroadcast(intent);
        }

        //LOAD 2 ADS
        Log.d("PARSE", "Starting the first 2 data parse");
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
        barDialog.setCancelable(true);
        barDialog.show();
        barDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                for(AsyncGetUrl asyncGetUrl : parsingAsyncTasks)
                {
                    asyncGetUrl.cancel(true);
                }
            }
        });
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

    private void startWaitingForPlayersCircle()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            circleDialog = new ProgressDialog(this, R.style.DialogThemePurple);
        else
            circleDialog = new ProgressDialog(this);
        circleDialog.setIndeterminate(true);
        circleDialog.setMessage("A esperar por jogadores");
        circleDialog.setCancelable(false);
        circleDialog.show();
    }


    @Override
    public void parsingEnded()
    {
        if(app.getAds()[0] != null && app.getAds()[1] != null)
        {
            circleDialog.dismiss();
            this.sendHostReady();
            startWaitingForPlayersCircle();
        }

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


    private void sendHostReady()
    {
        Intent intent = new Intent(getString(R.string.S004));
        intent.putExtra(getString(R.string.S004_HOSTREADY), "");
        sendBroadcast(intent);
    }


    private void startGameActivity()
    {
        Intent intent = new Intent(this, PriceGuessGameMultiplayerActivity.class);
        intent.putExtra(getString(R.string.S008_GAMESIZE), Settings.getGameSize());
        intent.putExtra(getString(R.string.S008_GAMEURLS), this.urls);
        intent.putExtra(getString(R.string.S008_HOSTINACTIVITY), true);
        intent.putExtra(getString(R.string.S008_PLAYERID), this.player.getPlayerId());
        startActivityForResult(intent,GAME_ACTIVITY_RESULT_CODE);
    }
}
