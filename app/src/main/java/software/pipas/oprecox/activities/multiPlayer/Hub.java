package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.InviteListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerLoader;
import software.pipas.oprecox.modules.dataType.Invite;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.network.AnnouncerSender;
import software.pipas.oprecox.modules.network.InviterReceiver;
import software.pipas.oprecox.util.Util;

public class Hub extends MultiplayerClass
{
    private ArrayList<Invite> invites;
    private InviteListAdapter inviteListAdapter;

    private AnnouncerSender announcerSender; //1 socket sending to broadcast:9999
    private DatagramSocket socket; //1 socket for receive of invites

    private int port;
    private Player player;

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


        //CREATING UNICAST SOCKET FOR RECEIVING INVITES, STARTING RECEIVER
        this.socket = this.createSocket();
        if(this.socket == null)
        {
            Toast.makeText(this, "Unable to receive invites, please try again!", Toast.LENGTH_SHORT);
            this.port = -1;
        }
        else
        {
            this.port = this.socket.getLocalPort();
            this.startInviterReceiver(this.socket);
        }
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
        if(this.announcerSender == null)
            this.startAnnouncer(name, player.getDisplayName(), player.getPlayerId(), this.port);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.announcerSender.close();
        this.socket.close();
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

        this.announcerSender = new AnnouncerSender(this.getApplicationContext(), name, displayName, playerId, invitePort);

        if(this.announcerSender.isValid())
        {
            this.announcerSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            Toast.makeText(this, "Cannot Announce", Toast.LENGTH_SHORT).show();
        }
    }

    private DatagramSocket createSocket()
    {
        DatagramSocket socket;
        try {socket =  new DatagramSocket(); return socket;}
        catch (SocketException e) {e.printStackTrace(); return null;}
    }

    private void startInviterReceiver(DatagramSocket socket)
    {
        InviterReceiver inviterReceiver = new InviterReceiver(this.getApplicationContext(), this, socket);
        inviterReceiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void registerReceived(DatagramPacket packet)
    {

        Message msg = new Message(this.getApplicationContext(), packet);
        //test self-announce REMOVE THE COMMENT LINE IN THE END
        if(player.getPlayerId().equals(msg.getPlayerId()) || this.inviteListAdapter == null || !msg.getMessageType().equals(MessageType.INVITE.toString())) return;



        Invite invite = new Invite(
                msg.getRoomName(),
                msg.getDisplayName(),
                msg.getName(),
                msg.getPlayerId(),
                msg.getRoomPort(),
                packet.getAddress(),
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
