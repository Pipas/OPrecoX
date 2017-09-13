package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.util.Util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class AnnouncerSenderService extends IntentService
{
    private Context context;
    private String name;
    private String displayName;
    private String playerId;
    private boolean closed;
    private Message message;
    private InetSocketAddress socketAddress;

    public AnnouncerSenderService() {super("AnnouncerSenderService");}

    public AnnouncerSenderService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.context = this.getApplicationContext();
        this.closed = false;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        Log.d("HANDEL","");
        this.name = intent.getExtras().getString(getResources().getString(R.string.S003_NAME));
        this.displayName = intent.getExtras().getString(getResources().getString(R.string.S003_DISPLAYNAME));
        this.playerId = intent.getExtras().getString(getResources().getString(R.string.S003_PLAYERID));

        if(this.createPacket())
            this.startLoop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.closed = true;
    }


    private boolean createPacket()
    {
        //TO CHANGE TO MESSAGE CLASS
        String[] args = new String[6];
        args[0] = getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.ANNOUNCE.toString();
        args[3] = Util.substituteSpace(this.name);
        args[4] = this.displayName;
        args[5] = this.playerId;

        this.message = new Message(this.context, args);
        if(!this.message.isValid()) return false;

        try
        {
            this.socketAddress = new InetSocketAddress(
                    InetAddress.getByName(getResources().getString(R.string.address_send)),
                    getResources().getInteger(R.integer.network_port));

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    private void startLoop()
    {
        while(!this.closed)
        {
            this.actualizePacketIP();
            this.sendPacket();
            this.sleep();
        }
    }

    private boolean sendPacket()
    {
        Intent intent = new Intent(getResources().getString(R.string.S000));
        intent.putExtra(getResources().getString(R.string.S000_MESSAGE), message.getMessage());
        intent.putExtra(getResources().getString(R.string.S000_INETSOCKETADDRESS), this.socketAddress);
        sendBroadcast(intent);
        return true;
    }

    private boolean sleep()
    {
        try
        {
            Thread.sleep(getResources().getInteger(R.integer.TIME_BETWEEN_SEND));
            return true;
        }
        catch (InterruptedException i)
        {
            return false;
        }
    }

    public void actualizePacketIP()
    {
        LinkedList<InetAddress> list = Util.listinterfaces();

        int port = getResources().getInteger(R.integer.network_port);

        //sets the latest broadcast or the default one
        if(list.size() > 0)
        {
            this.socketAddress = new InetSocketAddress(list.get(0), port);
        }
        else
        {
            try {this.socketAddress = new InetSocketAddress(InetAddress.getByName(getResources().getString(R.string.address_send)), port);}
            catch (UnknownHostException e) {e.printStackTrace();}
        }
    }
}
