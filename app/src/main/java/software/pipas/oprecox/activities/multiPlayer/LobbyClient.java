package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
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

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerImageLoader;
import software.pipas.oprecox.modules.interfaces.OnPlayerImageLoader;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;

public class LobbyClient extends MultiplayerClass implements OnPlayerImageLoader
{
    private static boolean loaded = false;

    private BroadcastReceiver broadcastReceiver;
    private Player player;

    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_client);

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

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);
        player = Games.Players.getCurrentPlayer(this.mGoogleApiClient);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.sendClosedtoHub();
        if(this.broadcastReceiver != null) unregisterReceiver(this.broadcastReceiver);
        loaded = false;
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

    public void handleReceivedIntent(Context context, Intent intent)
    {
        Log.d("RECEIVE_LOBBY", intent.toString());
        String str = intent.getExtras().getString(getResources().getString(R.string.S006_RESPONSE));
        if (str != null && str.equals(ResponseType.CLOSED.toString()))
        {
            finish();
            return;
        }

        str = intent.getExtras().getString(getResources().getString(R.string.S006_MESSAGE));
        if (str != null)
        {
            Message message = new Message(this.getApplicationContext(), str);

            if (message.getMessageType().equals(MessageType.ADDPLAYERLIST.toString()))
            {
                software.pipas.oprecox.modules.dataType.Player player;
                player = new software.pipas.oprecox.modules.dataType.Player(message.getName(), message.getDisplayName(), message.getPlayerId(), null);
                this.players.add(player);
                this.retrievePlayerURI(this.playerListAdapter, player);
                this.refreshListAdapter(this.playerListAdapter);
            }
            else if (message.getMessageType().equals(MessageType.REMOVEPLAYERLIST.toString()))
            {
                software.pipas.oprecox.modules.dataType.Player player = new software.pipas.oprecox.modules.dataType.Player(message.getPlayerId());
                this.players.remove(player);
                this.refreshListAdapter(this.playerListAdapter);
            }
            else if (message.getMessageType().equals(MessageType.ACTUALIZEROOMNAME.toString()))
            {
                Log.d("ROOM_NAME", message.getRoomName());
                TextView displayName = (TextView) findViewById(R.id.roomNameClient);
                displayName.setText(message.getRealRoomName());
            }

        }
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
}


