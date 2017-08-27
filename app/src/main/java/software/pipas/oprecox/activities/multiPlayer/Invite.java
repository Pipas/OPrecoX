package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerListUpdater;
import software.pipas.oprecox.modules.customThreads.PlayerLoader;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.network.AnnouncerReceiverService;
import software.pipas.oprecox.modules.network.InviterSender;
import software.pipas.oprecox.util.Util;

public class Invite extends MultiplayerClass
{

    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;

    private Intent announcerReceiverService; //1 socket receiving from broadcast:9999
    private PlayerListUpdater playerListUpdater;
    private DatagramSocket socket; //1 socket for sender of invites

    private Player player;

    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_invite);

        ListView listView = (ListView) findViewById(R.id.playersListViewer);
        this.players = new ArrayList<>();

        final PlayerListAdapter playerListAdapter = new PlayerListAdapter(this.players, getApplicationContext(), getContentResolver());
        listView.setAdapter(playerListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                software.pipas.oprecox.modules.dataType.Player player = playerListAdapter.getItem(position);
                Log.d("INVITE_DEBUG", player.toString());
                sendInvite(player);
            }
        });

        this.playerListAdapter = playerListAdapter;

        //initializing broadcast receiver
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String msg = intent.getExtras().getString(getResources().getString(R.string.ANNOUNCER_PACKET_MSG));
                InetAddress ip = (InetAddress) intent.getSerializableExtra(getResources().getString(R.string.ANNOUNCER_PACKET_IP));

                registerReceived(msg, ip);
            }
        };
        registerReceiver(this.broadcastReceiver, new IntentFilter(getResources().getString(R.string.ANNOUNCER_PACKET_ACTION)));


        //INVITER SENDER
        this.createSocketSender();

        //RECEIVER
        if(this.announcerReceiverService == null)
            this.startReceiver();

        //UPDATER
        if(this.playerListUpdater == null)
            this.startUpdater();

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        super.onConnected(bundle);
        this.player = Games.Players.getCurrentPlayer(mGoogleApiClient);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        stopService(this.announcerReceiverService);
        unregisterReceiver(this.broadcastReceiver);

        this.socket.close();
        this.playerListUpdater.close();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


    private void startReceiver()
    {
        this.announcerReceiverService = new Intent(this, AnnouncerReceiverService.class);
        startService(this.announcerReceiverService);
    }

    private void startUpdater()
    {
        this.playerListUpdater = new PlayerListUpdater(this, this.players, this.playerListAdapter);
        this.playerListUpdater.start();
    }


    public void registerReceived(String msgStr, InetAddress ip)
    {
        Log.d("INVITE_DEBUG", msgStr);
        Message msg = new Message(this.getApplicationContext(), msgStr);
        //test self-announce REMOVE THE COMMENT LINE IN THE END
        if(player == null || !msg.isValid() || player.getPlayerId().equals(msg.getPlayerId()) || this.playerListAdapter == null || !msg.getMessageType().equals(MessageType.ANNOUNCE.toString())) return;

        //the new player
        software.pipas.oprecox.modules.dataType.Player player =
                new software.pipas.oprecox.modules.dataType.Player(
                        msg.getName(),
                        msg.getDisplayName(),
                        msg.getPlayerId(),
                        null,
                        msg.getInvitePort(),
                        ip,
                        System.currentTimeMillis());


        int index = this.players.indexOf(player);

        //if does not exist add, else actualize
        if (index <= -1)
        {
            this.retrievePlayerURI(this.playerListAdapter, player);
            this.players.add(player);
            this.refreshListAdapter(this.playerListAdapter);
        }
        else
        {
            this.players.get(index).updatePlayerAddress(player.getAddress());
            this.players.get(index).updatePlayerInvitePort(player.getInvitePort());
            this.players.get(index).updatePlayerAnnouncedTime(player.getTimeAnnounced());
        }

    }



    private void refreshListAdapter(PlayerListAdapter playerListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(playerListAdapter);
        this.runOnUiThread(listAdapterRefresh);
    }

    private void retrievePlayerURI(PlayerListAdapter playerListAdapter, software.pipas.oprecox.modules.dataType.Player player)
    {
        PlayerLoader playerLoader = new PlayerLoader(this, this.mGoogleApiClient, playerListAdapter, player);
        playerLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void createSocketSender()
    {
        try {this.socket = new DatagramSocket();}
        catch (SocketException e) {e.printStackTrace(); this.socket = null;}
    }

    private void sendInvite(software.pipas.oprecox.modules.dataType.Player player)
    {
        //get stuff from the room asap
        String roomName = Util.substituteSpace("Room 1");//temp
        String hostDisplayName = this.player.getDisplayName();
        String hostName;

        if(this.player.getName() == null)
            hostName = hostDisplayName;
        else
            hostName = Util.substituteSpace(this.player.getName());

        String playerID = this.player.getPlayerId();
        String roomPort = "11111";//temp


        String[] args = new String[8];
        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.INVITE.toString();
        args[3] = roomName;
        args[4] = hostDisplayName;
        args[5] = hostName;
        args[6] = playerID;
        args[7] = roomPort;

        Message msg = new Message(this.getApplicationContext(), args);

        if(!msg.isValid())
        {
            Toast.makeText(this, "Unable to send Invite", Toast.LENGTH_SHORT).show();
            return;
        }

        DatagramPacket packet = new DatagramPacket(msg.getMessage().getBytes(), 0, msg.getMessage().getBytes().length);
        packet.setAddress(player.getAddress());
        packet.setPort(player.getInvitePort());

        InviterSender inviterSender = new InviterSender(this.getApplicationContext(), this, packet, this.socket);
        inviterSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
