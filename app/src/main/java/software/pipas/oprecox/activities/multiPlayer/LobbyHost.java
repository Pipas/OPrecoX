package software.pipas.oprecox.activities.multiPlayer;

import android.os.Bundle;


import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;

public class LobbyHost extends MultiplayerClass
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_host);
    }


}
