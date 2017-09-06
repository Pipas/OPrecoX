package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.message.ResponseType;

public class LobbyClient extends MultiplayerClass
{
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_client);

        this.startBroadcastReceiver();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleReceivedIntent(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S006));
        registerReceiver(this.broadcastReceiver, filter);
    }

    public void handleReceivedIntent(Context context, Intent intent)
    {
        String str = intent.getExtras().getString(getResources().getString(R.string.S006_RESPONSE));

        if(str != null && str.equals(ResponseType.CLOSED.toString())) finish();

    }




}
