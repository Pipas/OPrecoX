package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.dataType.*;

public class LobbyHost extends MultiplayerClass
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_host);
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
        startActivity(intent);
    }


}
