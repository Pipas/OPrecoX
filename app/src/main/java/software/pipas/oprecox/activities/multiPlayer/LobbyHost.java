package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.network.Room;

public class LobbyHost extends MultiplayerClass
{
    private final int OPTIONS_REQUEST_CODE = 1;

    private Intent room;
    private String roomName;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_host);
        this.startRoom();
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
        stopService(this.room);
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
        this.room = new Intent(this, Room.class);
        startService(this.room);
    }

}
