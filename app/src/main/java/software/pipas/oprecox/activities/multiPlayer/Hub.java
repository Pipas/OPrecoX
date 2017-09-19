package software.pipas.oprecox.activities.multiPlayer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.InviteListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerImageLoader;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Invite;
import software.pipas.oprecox.modules.interfaces.OnPlayerImageLoader;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;
import software.pipas.oprecox.modules.network.AnnouncerSenderService;
import software.pipas.oprecox.modules.network.ClientService;
import software.pipas.oprecox.modules.network.UDPCommsService;
import software.pipas.oprecox.util.Util;

public class Hub extends MultiplayerClass implements OnPlayerImageLoader
{
    private ArrayList<Invite> invites;
    private InviteListAdapter inviteListAdapter;
    private Player player;

    private Intent udpCommsService;
    private Intent announcerSenderService;
    private Intent tcpCommsService;
    private BroadcastReceiver broadcastReceiver;
    private ProgressDialog progressDialog;
    private ProgressDialog loadDialog;
    private Invite latestAcceptedInvite;
    private InetAddress myIP;

    private TextView multiplayerHubTooltip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_hub);

        initiateCustomFonts();

        DynamicListView listView = (DynamicListView) findViewById(R.id.list);
        invites = new ArrayList<>();

        final InviteListAdapter inviteListAdapter = new InviteListAdapter(invites, getApplicationContext(), getContentResolver());
        SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(inviteListAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        listView.enableSwipeToDismiss(
                new OnDismissCallback()
                {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions)
                        {
                            inviteListAdapter.remove(position);
                        }
                    }
                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Invite invite = invites.get(position);
                inviteListAdapter.remove(position);
                Log.d("LOG_DEBUG", invite.toString());
                acceptInvite(invite);
            }
        });

        Button hostButton = (Button) findViewById(R.id.hostButton);
        hostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hostButtonPressed();
            }
        });

        this.inviteListAdapter = inviteListAdapter;

        //start ProgressUpdater
        this.startProgressUpdater();

        //registering the broacastReceiver
        this.startBroadcastReceiver();

        //starting comms udp service
        this.startUDPCommsService();

        this.myIP = Util.listMyIP();

        this.initializeAndStartProgressDialog();
    }

    private void initiateCustomFonts()
    {
        TextView displayName = (TextView)findViewById(R.id.displayName);
        TextView ipOutput = (TextView)findViewById(R.id.realName);
        multiplayerHubTooltip = (TextView) findViewById(R.id.multiplayerHubTooltip);

        CustomFontHelper.setCustomFont(displayName, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(ipOutput, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(multiplayerHubTooltip, "font/Comfortaa_Regular.ttf", getBaseContext());
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);
        player = Games.Players.getCurrentPlayer(mGoogleApiClient);
        if(player == null)
        {
            Toast.makeText(this, "Failed to Log in to Google Play Services, please try again", Toast.LENGTH_SHORT);
            finish();
        }


        //STARTOING ONLY IF CONNECTION SUCCEDED
        //------------------------------------------------------------------------
        TextView displayName = (TextView) findViewById(R.id.displayName);
        displayName.setText(player.getDisplayName());

        TextView ipOutput = (TextView) findViewById(R.id.realName);
        ipOutput.setText("IP: " + this.myIP.toString().substring(1));


        ImageView imageView = (ImageView) findViewById(R.id.playerImage);

        if(imageView.getDrawable() == null)
        {
            ImageManager imageManager = ImageManager.create(this);
            imageManager.loadImage(imageView, player.getHiResImageUri());
        }


        String name = player.getName();
        if(name == null) name = player.getDisplayName();

        //starting the announcerSender service
        this.startAnnouncerSenderService(name, player.getDisplayName(), player.getPlayerId());

        this.loadDialog.dismiss();

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(this.broadcastReceiver != null) unregisterReceiver(this.broadcastReceiver);
        if(this.udpCommsService != null )stopService(this.udpCommsService);
        if(this.announcerSenderService != null) stopService(this.announcerSenderService);
        if(this.tcpCommsService != null) stopService(this.tcpCommsService);
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
        this.refreshListAdapter(this.inviteListAdapter);
    }


    public void hostButtonPressed()
    {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
        {
            Intent intent = new Intent(this, LobbyHost.class);
            startActivity(intent);
        }
    }

    private void startProgressUpdater()
    {
        this.progressDialog =  new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Trying to Establish Connection");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleReceivedIntent(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S001));
        registerReceiver(this.broadcastReceiver, filter);
    }

    private void startUDPCommsService()
    {
        this.udpCommsService = new Intent(this, UDPCommsService.class);
        startService(this.udpCommsService);
    }

    private void startAnnouncerSenderService(String name, String displayName, String playerID)
    {
        this.announcerSenderService = new Intent(this, AnnouncerSenderService.class);
        this.announcerSenderService.putExtra(getResources().getString(R.string.S003_NAME), name);
        this.announcerSenderService.putExtra(getResources().getString(R.string.S003_DISPLAYNAME), displayName);
        this.announcerSenderService.putExtra(getResources().getString(R.string.S003_PLAYERID), playerID);
        startService(this.announcerSenderService);
    }

    private void initializeAndStartProgressDialog()
    {
        this.loadDialog = new ProgressDialog(this);
        this.loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.loadDialog.setTitle("Por Favor Espera!");
        this.loadDialog.setMessage("A Carregar Jogador");
        this.loadDialog.setCancelable(false);
        this.loadDialog.show();
    }

    //callback for received intents with S001 action, only INVITE messages for now
    private void handleReceivedIntent(Context context, Intent intent)
    {

        String response = intent.getExtras().getString(getResources().getString(R.string.S001_RESPONSE));

        if(response != null)
        {
            this.progressDialog.dismiss();

            if(response.equals(ResponseType.TIMEOUT.toString()))
            {
                Toast.makeText(this, "Connection Timed Out!", Toast.LENGTH_SHORT).show();
                if(this.tcpCommsService != null) stopService(this.tcpCommsService);
            }
            else if(response.equals(ResponseType.CLOSED.toString()))
            {
                Toast.makeText(this, "Connection no longer available", Toast.LENGTH_SHORT).show();
                if(this.tcpCommsService != null) stopService(this.tcpCommsService);
            }
            else if(response.equals(ResponseType.CLIENT_CLOSED.toString()))
            {
                if(this.tcpCommsService != null) stopService(this.tcpCommsService);
            }
            else if(response.equals(ResponseType.CONNECTED.toString()))
            {
                Intent activity = new Intent(this, LobbyClient.class);
                if(this.latestAcceptedInvite != null)
                {
                    activity.putExtra(getString(R.string.S006_ROOMNAME), this.latestAcceptedInvite.getRealRoomName());
                    activity.putExtra(getString(R.string.S006_HOSTERNAME), this.latestAcceptedInvite.getDisplayName());
                    while (this.latestAcceptedInvite.getPlayerImage() == null) {}
                    activity.putExtra(getString(R.string.S006_ROOMIMAGE), this.latestAcceptedInvite.getPlayerImage());
                }
                startActivity(activity);
            }

            return;

        }


        String str = intent.getExtras().getString(getResources().getString(R.string.S001_MESSAGE));
        if(str != null)
        {
            Message msg = new Message(this.getApplicationContext(), str);

            if(msg.isValid() && player != null && !player.getPlayerId().equals(msg.getPlayerId()) && this.inviteListAdapter != null && msg.getMessageType().equals(MessageType.INVITE.toString()))
            {
                InetSocketAddress socketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S001_INETSOCKETADDRESS));
                if(socketAddress == null) return;

                Invite invite = new Invite(
                        msg.getRoomName(),
                        msg.getDisplayName(),
                        msg.getName(),
                        msg.getPlayerId(),
                        msg.getRoomPort(),
                        socketAddress.getAddress(),
                        System.currentTimeMillis(),
                        null);


                int index = this.invites.indexOf(invite);

                if(index <= -1)
                {
                    this.retrievePlayerURI(this.inviteListAdapter, invite);
                    this.invites.add(invite);
                    this.refreshListAdapter(this.inviteListAdapter);
                }
                else
                {
                    this.invites.get(index).updateAddress(invite.getAddress());
                    this.invites.get(index).updateRoomPort(invite.getRoomPort());
                    this.invites.get(index).updateTimeReceived(invite.getTimeReceived());

                    if(!this.invites.get(index).getRoomName().equals(invite.getRoomName()))
                    {
                        this.invites.get(index).updateRoomName(invite.getRoomName());
                        this.refreshListAdapter(this.inviteListAdapter);
                    }
                }

            }
            else if (msg.isValid() && player != null && msg.getMessageType().equals(MessageType.REQUESTID.toString()))
            {
                String[] args = new String[4];

                args[0] = this.getString(R.string.network_app_name);
                args[1] = Integer.toString(BuildConfig.VERSION_CODE);
                args[2] = MessageType.ID.toString();
                args[3] = this.player.getPlayerId();

                Message newMsg = new Message(this.getApplicationContext(), args);

                if (!newMsg.isValid()) return;

                Intent intent1 = new Intent(getResources().getString(R.string.S005));
                intent1.putExtra(getResources().getString(R.string.S005_MESSAGE), newMsg.getMessage());
                sendBroadcast(intent1);
            }
            else if (msg.isValid() && player != null && msg.getMessageType().equals(MessageType.ADDPLAYERLIST.toString()))
            {
                Log.d("RECEIVE_HUB", msg.getMessage());
            }
        }

    }

    private void acceptInvite(Invite invite)
    {
        if(this.tcpCommsService != null) stopService(this.tcpCommsService);

        this.latestAcceptedInvite = invite;
        this.progressDialog.show();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(invite.getAddress(), invite.getRoomPort());
        this.tcpCommsService = new Intent(this, ClientService.class);
        this.tcpCommsService.putExtra(getResources().getString(R.string.S005_INETSOCKETADDRESS), inetSocketAddress);
        startService(this.tcpCommsService);
    }

    private void retrievePlayerURI(InviteListAdapter inviteListAdapter, Invite invite)
    {
        PlayerImageLoader playerImageLoader = new PlayerImageLoader(Hub.this, this.mGoogleApiClient, inviteListAdapter, invite);
        playerImageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void refreshListAdapter(InviteListAdapter inviteListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(inviteListAdapter, multiplayerHubTooltip, null);
        this.runOnUiThread(listAdapterRefresh);
    }


}
