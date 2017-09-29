package software.pipas.oprecox.activities.multiPlayer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.util.ArrayList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerImageLoader;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.interfaces.OnPlayerImageLoader;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;
import software.pipas.oprecox.modules.parsing.AsyncGetAd;
import software.pipas.oprecox.modules.parsing.OlxParser;
import software.pipas.oprecox.util.Settings;

public class LobbyClient extends MultiplayerClass implements OnPlayerImageLoader, ParsingCallingActivity
{
    private static boolean loaded;
    private final int GAME_ACTIVITY_RESULT_CODE = 1;

    private BroadcastReceiver broadcastReceiver;
    private Player player;

    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;

    private ArrayList<String> urls;

    private OPrecoX app;
    private ProgressDialog circleDialog;
    private OlxParser olxParser;
    private Boolean blocked;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_client);

        this.app = (OPrecoX) this.getApplicationContext();

        initiateCustomFonts();

        String roomName = getIntent().getExtras().getString(getString(R.string.S006_ROOMNAME));
        String hosterName = getIntent().getExtras().getString(getString(R.string.S006_HOSTERNAME));
        Uri playerIamge = getIntent().getExtras().getParcelable(getString(R.string.S006_ROOMIMAGE));

        //load room image
        ImageView imageView = (ImageView) findViewById(R.id.roomImageClient);
        if(imageView.getDrawable() == null)
        {
            ImageManager imageManager = ImageManager.create(this);
            imageManager.loadImage(imageView, playerIamge);
        }

        //load host name
        TextView displayName = (TextView) findViewById(R.id.roomNameClient);
        displayName.setText(roomName);

        TextView roomNameView = (TextView) findViewById(R.id.roomHostNameClient);
        roomNameView.setText(hosterName);


        ListView listView = (ListView) findViewById(R.id.playersListViewer);
        this.players = new ArrayList<>();

        final PlayerListAdapter playerListAdapter = new PlayerListAdapter(this.players, getApplicationContext(), getContentResolver());
        listView.setAdapter(playerListAdapter);

        this.playerListAdapter = playerListAdapter;

        this.startBroadcastReceiver();
    }

    private void initiateCustomFonts()
    {
        TextView roomName = (TextView)findViewById(R.id.roomNameClient);
        TextView roomHostName = (TextView)findViewById(R.id.roomHostNameClient);

        CustomFontHelper.setCustomFont(roomName, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(roomHostName, "font/antipastopro-demibold.otf", getBaseContext());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);
        player = Games.Players.getCurrentPlayer(this.mGoogleApiClient);

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        shutdown();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.sendClosedtoHub();
        if(this.broadcastReceiver != null) unregisterReceiver(this.broadcastReceiver);

    }

    private void shutdown()
    {
        loaded = false;
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        Log.d("RESULT_INTENT", requestCode + " " + resultCode);
        if(requestCode == GAME_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK)
        {
            Log.d("RESULT_INTENT", "reached");

            String toFinishLobby = intent.getExtras().getString(getString(R.string.S006_CLIENTFORCEEXITGAME));

            if(toFinishLobby != null)
            {
                shutdown();
            }
        }
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleReceivedIntent(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S006));
        registerReceiver(this.broadcastReceiver, filter);
        loaded = true;
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

    public void handleReceivedIntent(Context context, Intent intent)
    {
        String str = intent.getExtras().getString(getResources().getString(R.string.S006_RESPONSE));
        if (str != null && str.equals(ResponseType.CLOSED.toString()))
        {
            shutdown();
            return;
        }

        str = intent.getExtras().getString(getResources().getString(R.string.S006_MESSAGE));
        if (str != null)
        {
            Log.d("CLIENT_DEBUG",str);
            Message message = new Message(this.getApplicationContext(), str);

            if (message.isValid() && message.getMessageType().equals(MessageType.ADDPLAYERLIST.toString()))
            {
                software.pipas.oprecox.modules.dataType.Player player;
                player = new software.pipas.oprecox.modules.dataType.Player(message.getName(), message.getDisplayName(), message.getPlayerId(), null);
                this.checkExistsAndAdd(player);
                this.retrievePlayerURI(this.playerListAdapter, player);
                this.refreshListAdapter(this.playerListAdapter);
            }
            else if (message.isValid() && message.getMessageType().equals(MessageType.REMOVEPLAYERLIST.toString()))
            {
                software.pipas.oprecox.modules.dataType.Player player = new software.pipas.oprecox.modules.dataType.Player(message.getPlayerId());
                this.removePlayer(player);
                this.refreshListAdapter(this.playerListAdapter);
            }
            else if (message.isValid() && message.getMessageType().equals(MessageType.ACTUALIZEROOMNAME.toString()))
            {
                Log.d("ROOM_NAME", message.getRoomName());
                TextView displayName = (TextView) findViewById(R.id.roomNameClient);
                displayName.setText(message.getRealRoomName());
            }
            else if(message.isValid() && message.getMessageType().equals(MessageType.GAMEURLS.toString()))
            {
                //GOT URLS START LOADING 2
                Log.d("URLS_DEBUG", "client gets urls");
                this.olxParser = new OlxParser();
                this.urls = message.getUrlsArrayList();
                this.gotURLStartLoading();
            }
            else if(message.isValid() && message.getMessageType().equals(MessageType.STARTGAME.toString()))
            {
                circleDialog.dismiss();
                this.startGameActivity();
            }

        }
    }

    private synchronized void checkExistsAndAdd(software.pipas.oprecox.modules.dataType.Player player)
    {
        for (software.pipas.oprecox.modules.dataType.Player player1 : this.players)
        {
            if(player.equals(player1)) return;
        }

        this.players.add(player);
    }

    private synchronized void removePlayer(software.pipas.oprecox.modules.dataType.Player player)
    {
        this.players.remove(player);
    }

    private void retrievePlayerURI(PlayerListAdapter playerListAdapter, software.pipas.oprecox.modules.dataType.Player player)
    {
        PlayerImageLoader playerImageLoader = new PlayerImageLoader(LobbyClient.this, this.mGoogleApiClient, playerListAdapter, player);
        playerImageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRefreshUI()
    {
        this.refreshListAdapter(this.playerListAdapter);
    }

    private void refreshListAdapter(PlayerListAdapter playerListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(playerListAdapter, null, null);
        this.runOnUiThread(listAdapterRefresh);
    }

    private void sendClosedtoHub()
    {
        Intent intent = new Intent(getString(R.string.S001));
        intent.putExtra(getString(R.string.S001_RESPONSE), ResponseType.CLIENT_CLOSED.toString());
        sendBroadcast(intent);

    }

    public static boolean isLoaded() {return loaded;}

    private void gotURLStartLoading()
    {
        Settings.setGameSize(this.urls.size(), this);
        app.setAds(new Ad[Settings.getGameSize()]);
        AsyncGetAd parsingAsyncTask;
        startProgressCircle();
        for(int i = 0; i < 2; i++)
        {
            parsingAsyncTask = new AsyncGetAd(this, app, urls.get(i), i, olxParser);
            parsingAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void parsingEnded()
    {
        if(app.getAds()[0] != null && app.getAds()[1] != null)
        {
            circleDialog.dismiss();
            this.startWaitingForHost();
            this.sendReadyMessage();
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
            shutdown();
        }
    }

    private void startWaitingForHost()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            circleDialog = new ProgressDialog(this, R.style.DialogThemePurple);
        else
            circleDialog = new ProgressDialog(this);
        circleDialog.setIndeterminate(true);
        circleDialog.setMessage("A esperar pelo Host");
        circleDialog.setCancelable(false);
        circleDialog.show();
    }

    private void sendReadyMessage()
    {
        if(this.player == null) return;

        String[] args = new String[4];

        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.READY.toString();
        args[3] = this.player.getPlayerId();

        Message msg = new Message(this, args);

        Log.d("READY_DEBUG", "message created");

        if(!msg.isValid()) return;

        Log.d("READY_DEBUG", "message valid, send to ClientService");

        Intent intent = new Intent(getString(R.string.S005));
        intent.putExtra(getString(R.string.S005_MESSAGE), msg.getMessage());
        sendBroadcast(intent);
    }

    private void startGameActivity()
    {
        Intent intent = new Intent(this, PriceGuessGameMultiplayerActivity.class);
        intent.putExtra(getString(R.string.S008_GAMESIZE), this.urls.size());
        intent.putExtra(getString(R.string.S008_GAMEURLS), this.urls);
        intent.putExtra(getString(R.string.S008_HOSTINACTIVITY), false);
        intent.putExtra(getString(R.string.S008_PLAYERID), this.player.getPlayerId());
        startActivityForResult(intent, GAME_ACTIVITY_RESULT_CODE);
    }


}


