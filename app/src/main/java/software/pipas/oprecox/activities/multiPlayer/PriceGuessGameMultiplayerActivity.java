package software.pipas.oprecox.activities.multiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.singlePlayer.GameActivity;

/**
 * Created by nuno on 25-09-2017.
 */

public class PriceGuessGameMultiplayerActivity extends GameActivity
{
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);

        gameSize = getIntent().getIntExtra(getString(R.string.S008_GAMESIZE), 10);

        this.startBroadcastReceiver();

        startDataParses();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleIntentReceived(context, intent);
            }
        };

        IntentFilter filter = new IntentFilter(getString(R.string.S008));
        registerReceiver(this.broadcastReceiver, filter);

    }

    private void handleIntentReceived(Context context, Intent intent)
    {

    }
}
