package software.pipas.oprecox.activities.multiPlayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerListUpdater;
import software.pipas.oprecox.modules.customThreads.PlayerImageLoader;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.interfaces.OnPlayerImageLoader;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.util.Util;

public class Invite extends MultiplayerClass implements OnPlayerImageLoader {

    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;
    private PlayerListUpdater playerListUpdater;
    private Player player;
    private String roomName;

    private BroadcastReceiver broadcastReceiver;
    private TextView inviteTooltip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_invite);

        this.roomName = getIntent().getExtras().getString(getResources().getString(R.string.roomName));

        initiateCustomFonts();

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

        Button inviteButton = (Button) findViewById(R.id.inviteButton);
        inviteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Invite.this);
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(Invite.this);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final String m_Text = input.getText().toString();
                        (new Thread()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    InetAddress address = InetAddress.getByName(m_Text);
                                    if(address instanceof Inet4Address)
                                    {
                                        sendInvite(address);
                                        finish();
                                    }
                                    else {throw new UnknownHostException();}
                                }
                                catch (UnknownHostException e)
                                {
                                    Toast.makeText(getApplicationContext(), "IP invalido", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        this.playerListAdapter = playerListAdapter;

        //start BroadcastReceiver
        this.startBroadcastReceiver();
    }

    private void initiateCustomFonts()
    {
        TextView invitesTitleTextView = (TextView)findViewById(R.id.invitesTitleTextView);
        //inviteTooltip = (TextView)findViewById(R.id.inviteTooltip);

        CustomFontHelper.setCustomFont(invitesTitleTextView, "font/antipastopro-demibold.otf", getBaseContext());
        //CustomFontHelper.setCustomFont(inviteTooltip, "font/Comfortaa_Regular.ttf", getBaseContext());
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        super.onConnected(bundle);
        this.player = Games.Players.getCurrentPlayer(mGoogleApiClient);

        //UPDATER
        if(this.playerListUpdater == null)
            this.startUpdater();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(this.playerListUpdater != null) this.playerListUpdater.close();
        if(this.broadcastReceiver != null) unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRefreshUI()
    {
        this.refreshListAdapter(this.playerListAdapter);
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleReceivedIntent(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S002));
        registerReceiver(this.broadcastReceiver, filter);
    }

    private void startUpdater()
    {
        this.playerListUpdater = new PlayerListUpdater(this.getApplicationContext(),Invite.this, this.players);
        this.playerListUpdater.start();
    }

    //callback for recived intents with S002 action, only ANNOUNCE messages for now
    private void handleReceivedIntent(Context context, Intent intent)
    {
        String message = intent.getExtras().getString(getResources().getString(R.string.S002_MESSAGE));
        InetSocketAddress socketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S002_INETSOCKETADDRESS));

        Message msg = new Message(this.getApplicationContext(), message);
        //test self-announce REMOVE THE COMMENT LINE IN THE END
        if(!msg.isValid() || player == null ||player.getPlayerId().equals(msg.getPlayerId()) || this.playerListAdapter == null || !msg.getMessageType().equals(MessageType.ANNOUNCE.toString())) return;

        //the new player
        software.pipas.oprecox.modules.dataType.Player player =
                new software.pipas.oprecox.modules.dataType.Player(
                        msg.getName(),
                        msg.getDisplayName(),
                        msg.getPlayerId(),
                        null,
                        socketAddress.getPort(),
                        socketAddress.getAddress(),
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
        PlayerImageLoader playerImageLoader = new PlayerImageLoader(Invite.this, this.mGoogleApiClient, playerListAdapter, player);
        playerImageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void sendInvite(software.pipas.oprecox.modules.dataType.Player remotePlayer)
    {
        //get stuff from the room asap
        String roomName = Util.substituteSpace(this.roomName);//temp
        String hostDisplayName = this.player.getDisplayName();
        String hostName;

        if(this.player.getName() == null)
            hostName = hostDisplayName;
        else
            hostName = Util.substituteSpace(this.player.getName());

        String playerID = this.player.getPlayerId();
        String roomPort = "-1";//temp


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

        InetSocketAddress socketAddress = new InetSocketAddress(remotePlayer.getAddress(), remotePlayer.getInvitePort());

        Intent intent = new Intent(getResources().getString(R.string.S004));
        intent.putExtra(getResources().getString(R.string.S004_MESSAGE), msg.getMessage());
        intent.putExtra(getResources().getString(R.string.S004_INETSOCKETADDRESS), socketAddress);
        intent.putExtra(getResources().getString(R.string.S004_PLAYER),remotePlayer);
        sendBroadcast(intent);

    }

    private void sendInvite(InetAddress address)
    {
        //get stuff from the room asap
        String roomName = Util.substituteSpace(this.roomName);//temp
        String hostDisplayName = this.player.getDisplayName();
        String hostName;

        if(this.player.getName() == null)
            hostName = hostDisplayName;
        else
            hostName = Util.substituteSpace(this.player.getName());

        String playerID = this.player.getPlayerId();
        String roomPort = "-1";//temp


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

        InetSocketAddress socketAddress = new InetSocketAddress(address, getResources().getInteger(R.integer.network_port));

        Intent intent = new Intent(getResources().getString(R.string.S004));
        intent.putExtra(getResources().getString(R.string.S004_MESSAGE), msg.getMessage());
        intent.putExtra(getResources().getString(R.string.S004_INETSOCKETADDRESS), socketAddress);
        sendBroadcast(intent);
    }


}
