package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.InviteListAdapter;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.dataType.Invite;

public class Hub extends MultiplayerClass
{
    private ArrayList<Invite> invites;

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

        ListView listView = (ListView) findViewById(R.id.list);
        invites = new ArrayList<>();

        for(int i = 1; i < 7; i++)
            invites.add(new Invite("Room number " + i, player.getName(), player.getHiResImageUri()));

        InviteListAdapter inviteListAdapter = new InviteListAdapter(invites, getApplicationContext());
        listView.setAdapter(inviteListAdapter);
    }

    public void hostButtonPressed(View view)
    {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
        {
            Intent intent = new Intent(this, LobbyHost.class);
            startActivity(intent);
        }
    }


}
