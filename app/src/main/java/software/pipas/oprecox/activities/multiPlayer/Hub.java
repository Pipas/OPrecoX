package software.pipas.oprecox.activities.multiPlayer;

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
import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.InviteListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerLoader;
import software.pipas.oprecox.modules.dataType.Invite;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.network.AnnouncerReceiverService;
import software.pipas.oprecox.modules.network.AnnouncerSender;
import software.pipas.oprecox.modules.network.AnnouncerSenderService;
import software.pipas.oprecox.modules.network.InviterReceiverService;

public class Hub extends MultiplayerClass
{
    private ArrayList<Invite> invites;
    private InviteListAdapter inviteListAdapter;

    private Intent announcerSenderService; //1 socket sending to broadcast:9999
    private Intent inviterReceiverService;

    private int port;
    private Player player;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_hub);

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
                Log.d("LOG_DEBUG", invite.toString());
            }
        });

        this.inviteListAdapter = inviteListAdapter;
        this.port = getResources().getInteger(R.integer.invite_port);
        this.startInviterReceiver(this.port);


        //broadcast receiver
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String msg = intent.getExtras().getString(getResources().getString(R.string.INVITER_RECEIVER_MSG));
                InetAddress ip = (InetAddress) intent.getSerializableExtra(getResources().getString(R.string.INVITER_RECEIVER_IP));

                registerReceived(msg, ip);
            }
        };
        registerReceiver(this.broadcastReceiver, new IntentFilter(getResources().getString(R.string.INVITER_RECEIVER_ACTION)));
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

        TextView realName = (TextView) findViewById(R.id.realName);
        realName.setText(player.getName());


        ImageView imageView = (ImageView) findViewById(R.id.playerImage);

        if(imageView.getDrawable() == null)
        {
            ImageManager imageManager = ImageManager.create(this);
            imageManager.loadImage(imageView, player.getHiResImageUri());
        }


        String name = player.getName();
        if(name == null) name = player.getDisplayName();


        //ANNOUNCER
        if(this.announcerSenderService == null)
            this.startAnnouncer(name, player.getDisplayName(), player.getPlayerId(), this.port);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopService(this.announcerSenderService);
        unregisterReceiver(this.broadcastReceiver);
        stopService(this.inviterReceiverService);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


    public void hostButtonPressed(View view)
    {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
        {
            Intent intent = new Intent(this, LobbyHost.class);
            startActivity(intent);
        }
    }

    public void startAnnouncer(String name, String displayName, String playerId, int invitePort)
    {
        this.announcerSenderService = new Intent(this, AnnouncerSenderService.class);
        this.announcerSenderService.putExtra(getResources().getString(R.string.ANNOUNCER_SENDER_NAME), name);
        this.announcerSenderService.putExtra(getResources().getString(R.string.ANNOUNCER_SENDER_DISPLAYNAME), displayName);
        this.announcerSenderService.putExtra(getResources().getString(R.string.ANNOUNCER_SENDER_PLAYERID), playerId);
        this.announcerSenderService.putExtra(getResources().getString(R.string.ANNOUNCER_SENDER_INVITEPORT), port);
        startService(this.announcerSenderService);
    }


    private void startInviterReceiver(int port)
    {
        this.inviterReceiverService = new Intent(this, InviterReceiverService.class);
        this.inviterReceiverService.putExtra(getResources().getString(R.string.INVITER_RECEIVER_PORT), this.port);
        startService(this.inviterReceiverService);
    }

    public void registerReceived(String msgStr, InetAddress ip)
    {
        Log.d("INVITE_RECEIVED_DEBUG", msgStr);
        Message msg = new Message(this.getApplicationContext(), msgStr);
        //test self-announce REMOVE THE COMMENT LINE IN THE END
        if(player == null || !msg.isValid() || player.getPlayerId().equals(msg.getPlayerId()) || this.inviteListAdapter == null || !msg.getMessageType().equals(MessageType.INVITE.toString())) return;



        Invite invite = new Invite(
                msg.getRoomName(),
                msg.getDisplayName(),
                msg.getName(),
                msg.getPlayerId(),
                msg.getRoomPort(),
                ip,
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

    private void retrievePlayerURI(InviteListAdapter inviteListAdapter, Invite invite)
    {
        PlayerLoader playerLoader = new PlayerLoader(this, this.mGoogleApiClient, inviteListAdapter, invite);
        playerLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void refreshListAdapter(InviteListAdapter inviteListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(inviteListAdapter);
        this.runOnUiThread(listAdapterRefresh);
    }

}
