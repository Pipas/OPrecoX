package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.InviteListAdapter;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.network.RoomService;

public class LobbyHost extends MultiplayerClass
{
    private final int OPTIONS_REQUEST_CODE = 1;

    private BroadcastReceiver broadcastReceiver;

    private Intent room;
    private String roomName;
    private Player player;

    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_host);
        this.startRoom();

        ListView listView = (ListView) findViewById(R.id.playersListViewer);
        this.players = new ArrayList<>();

        final PlayerListAdapter playerListAdapter = new PlayerListAdapter(this.players, getApplicationContext(), getContentResolver());
        listView.setAdapter(playerListAdapter);

        this.playerListAdapter = playerListAdapter;

        this.startBroadcastReceiver();
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
            Log.d("LOBBY_DEBUG", this.roomName);
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
    }

    private void refreshListAdapter(PlayerListAdapter playerListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(playerListAdapter);
        this.runOnUiThread(listAdapterRefresh);
    }
}
