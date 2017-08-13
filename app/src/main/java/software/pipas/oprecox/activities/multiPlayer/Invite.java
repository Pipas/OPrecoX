package software.pipas.oprecox.activities.multiPlayer;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.InviteListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.network.Receiver;

public class Invite extends MultiplayerClass {

    private ArrayList<software.pipas.oprecox.modules.dataType.Invite> invites;
    private Receiver receiver;

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
        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);

        //DEBUG
        DynamicListView listView = (DynamicListView) findViewById(R.id.inviteListViewer);
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


        //RECEIVER
        if(this.receiver == null)
            this.startReceiver(player.getPlayerId());
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.receiver.close();
    }




    private void startReceiver(String playerId)
    {
        this.receiver = new Receiver(this.getApplicationContext(), playerId);

        if(this.receiver.isValid())
        {
            this.receiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            Toast.makeText(this, "Cannot Receive", Toast.LENGTH_SHORT).show();
        }
    }

}
