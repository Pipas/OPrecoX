package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.dataType.Invite;
import software.pipas.oprecox.modules.network.AnnouncerSender;

public class Hub extends MultiplayerClass
{
    private ArrayList<Invite> invites;
    private AnnouncerSender announcerSender;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_hub);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);
        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);

        TextView displayName = (TextView) findViewById(R.id.displayName);
        displayName.setText(player.getDisplayName());

        TextView realName = (TextView) findViewById(R.id.realName);
        realName.setText(player.getName());

        ImageView imageView = (ImageView) findViewById(R.id.playerImage);
        ImageManager imageManager = ImageManager.create(this);
        imageManager.loadImage(imageView, player.getHiResImageUri());

        /*
        DynamicListView listView = (DynamicListView) findViewById(R.id.list);
        invites = new ArrayList<>();

        for(int i = 1; i < 7; i++)
            invites.add(new Invite("Room number " + i, player.getName(), player.getHiResImageUri()));

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
        */

        //ANNOUNCER
        if(this.announcerSender == null)
            this.startAnnouncer(player.getDisplayName(),player.getPlayerId(), player.getIconImageUri().toString());
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.announcerSender.close();

    }


    public void hostButtonPressed(View view)
    {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
        {
            Intent intent = new Intent(this, LobbyHost.class);
            startActivity(intent);
        }
    }

    public void startAnnouncer(String playerName, String playerId, String playerIcon)
    {
        this.announcerSender = new AnnouncerSender(this.getApplicationContext(), playerName, playerId, playerIcon);

        if(this.announcerSender.isValid())
        {
            this.announcerSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            Toast.makeText(this, "Cannot Announce", Toast.LENGTH_SHORT).show();
        }
    }


}
