package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;

/**
 * Created by nuno_ on 31-Aug-17.
 */

public class Room extends IntentService
{

    public Room() {super("Room");}

    public Room(String name) {super(name);}

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {

            }
        }, new IntentFilter("ok"));
    }

}
