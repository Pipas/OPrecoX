package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.InetSocketAddress;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;

/**
 * Created by nuno_ on 31-Aug-17.
 */

public class Room extends IntentService
{
    private BroadcastReceiver broadcastReceiver;
    private boolean closed;
    private int time;

    public Room() {super("Room");}
    public Room(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.time = getResources().getInteger(R.integer.TIME_BETWEEN_UPDATES);
        this.closed = false;
        this.initializeBroadcastReceiver();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        while (!this.closed)
        {
            this.sleep();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.closed = true;
        unregisterReceiver(this.broadcastReceiver);
    }



    public void initializeBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleIntentReceived(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S004));
        registerReceiver(this.broadcastReceiver, filter);
    }

    //receiver for Room, receive of Pre-Invites to send Invites
    public void handleIntentReceived(Context context, Intent intent)
    {
        String message = intent.getExtras().getString(getResources().getString(R.string.S004_MESSAGE));
        InetSocketAddress socketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S004_INETSOCKETADDRESS));

        Message msg = new Message(this.getApplicationContext(), message);

        if(!msg.isValid()) return;

        String[] args = new String[8];
        args[0] = msg.getAppName();
        args[1] = msg.getAppVersion();
        args[2] = msg.getMessageType();
        args[3] = msg.getRoomName();
        args[4] = msg.getDisplayName();
        args[5] = msg.getDisplayName();
        args[6] = msg.getPlayerId();
        args[7] = this.requestPort();

        Message newMsg = new Message(this.getApplicationContext(), args);

        if(!newMsg.isValid()) return;


        Intent newIntent = new Intent(getResources().getString(R.string.S000));
        newIntent.putExtra(getResources().getString(R.string.S000_MESSAGE), newMsg.getMessage());
        newIntent.putExtra(getResources().getString(R.string.S000_INETSOCKETADDRESS), socketAddress);
        sendBroadcast(newIntent);
    }

    public String requestPort()
    {
        return "1111";
    }

    public void sleep()
    {
        try {Thread.sleep(this.time);}
        catch (InterruptedException e) {e.printStackTrace();}
    }





}
