package software.pipas.oprecox.activities.multiPlayer;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.net.DatagramPacket;
import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.customThreads.ListAdapterRefresh;
import software.pipas.oprecox.modules.customThreads.PlayerListUpdater;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.network.AnnouncerReceiver;

public class Invite extends MultiplayerClass {


    private AnnouncerReceiver announcerReceiver;
    private PlayerListUpdater playerListUpdater;

    private Player player;
    private ArrayList<software.pipas.oprecox.modules.dataType.Player> players;
    private PlayerListAdapter playerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        super.onConnected(bundle);
        player = Games.Players.getCurrentPlayer(mGoogleApiClient);

        //DEBUG
        DynamicListView listView = (DynamicListView) findViewById(R.id.playersListViewer);
        players = new ArrayList<>();

        final PlayerListAdapter playerListAdapter = new PlayerListAdapter(players, getApplicationContext(), getContentResolver());
        SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(playerListAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);



        listView.enableSwipeToDismiss(
                new OnDismissCallback()
                {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions)
                        {
                            playerListAdapter.remove(position);
                        }
                    }
                }
        );

        this.playerListAdapter = playerListAdapter;


        //RECEIVER
        if(this.announcerReceiver == null)
            this.startReceiver();

        //UPDATER
        if(this.playerListUpdater == null)
            this.startUpdater();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.announcerReceiver.close();
    }


    private void startReceiver()
    {
        this.announcerReceiver = new AnnouncerReceiver(this.getApplicationContext(),this);

        if(this.announcerReceiver.isValid())
        {
            this.announcerReceiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            Toast.makeText(this, "Cannot Receive", Toast.LENGTH_SHORT).show();
        }
    }

    private void startUpdater()
    {
        this.playerListUpdater = new PlayerListUpdater(this, this.players, this.playerListAdapter);
        this.playerListUpdater.start();
    }

    @Override
    public void registerReceived(DatagramPacket packet)
    {
        Message msg = new Message(this.getApplicationContext(), packet);

        //test self-announce REMOVE THE COMMENT LINE IN THE END
        if(/*player.getPlayerId().equals(msg.getPlayerId()) || */this.playerListAdapter == null || !msg.getMessageType().equals(MessageType.ANNOUNCE.toString())) return;


        //the new player
        software.pipas.oprecox.modules.dataType.Player player =
                new software.pipas.oprecox.modules.dataType.Player(
                        msg.getPlayerName(),
                        msg.getPlayerId(),
                        Uri.parse(msg.getPlayerIconURI()),
                        System.currentTimeMillis());


        int index = this.players.indexOf(player);

        //if does not exist add, else actualize
        if(index <= -1)
        {
            this.players.add(player);
            this.refreshListAdapter(this.playerListAdapter);
        }
        else
        {
            this.players.get(index).updatePlayerAnnouncedTime(player.getTimeAnnounced());
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.playerListUpdater.close();
        finish();
    }

    private void refreshListAdapter(PlayerListAdapter playerListAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(playerListAdapter);
        this.runOnUiThread(listAdapterRefresh);
    }

}
