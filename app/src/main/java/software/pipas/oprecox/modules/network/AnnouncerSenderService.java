package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.util.Util;

/**
 * Created by nuno_ on 27-Aug-17.
 */

public class AnnouncerSenderService extends IntentService {

    private Context context;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private boolean closed;

    private String name;
    private String displayName;
    private String playerId;
    private int invitePort;

    public AnnouncerSenderService() {
        super("AnnouncerSenderService");
    }


    public AnnouncerSenderService(String name) {
        super(name);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.context = this.getApplicationContext();
        this.closed = false;
        this.createSocket();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.closed = true;
        this.socket.close();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        this.name = intent.getExtras().getString(getResources().getString(R.string.ANNOUNCER_SENDER_NAME));
        this.displayName = intent.getExtras().getString(getResources().getString(R.string.ANNOUNCER_SENDER_DISPLAYNAME));
        this.playerId = intent.getExtras().getString(getResources().getString(R.string.ANNOUNCER_SENDER_PLAYERID));
        this.invitePort = intent.getExtras().getInt(getResources().getString(R.string.ANNOUNCER_SENDER_INVITEPORT));
        this.createPacket();
        this.startLoop();
    }

    private boolean createSocket()
    {
        try
        {
            this.socket = new DatagramSocket();
            socket.setBroadcast(true);
            return true;
        }
        catch (SocketException s)
        {
            s.printStackTrace();

            this.socket.close();
            return false;
        }

    }

    private boolean createPacket()
    {
        //TO CHANGE TO MESSAGE CLASS
        String[] args = new String[7];
        args[0] = this.context.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.ANNOUNCE.toString();
        args[3] = Util.substituteSpace(this.name);
        args[4] = this.displayName;
        args[5] = this.playerId;
        args[6] = Integer.toString(this.invitePort);

        Message msg = new Message(this.context, args);
        if(!msg.isValid()) return false;

        try
        {
            this.packet = new DatagramPacket(msg.getMessage().getBytes(), 0, msg.getMessage().getBytes().length);

            //setting default broadcast ip, later to be changed programmatically
            this.packet.setSocketAddress(new InetSocketAddress(
                    InetAddress.getByName(this.context.getString(R.string.address_send)),
                    this.context.getResources().getInteger(R.integer.network_port)));


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
        try
        {
            this.socket.send(this.packet);
            return true;
        }
        catch (IOException i)
        {
            return false;
        }

    }

    private boolean sleep()
    {
        try
        {
            Thread.sleep(this.context.getResources().getInteger(R.integer.TIME_BETWEEN_SEND));
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

        //sets the latest broadcast or the default one
        if(list.size() > 0)
        {
            this.packet.setAddress(list.get(0));
        }
        else
        {
            try {this.packet.setAddress(InetAddress.getByName(this.context.getString(R.string.address_send)));}
            catch (UnknownHostException e) {e.printStackTrace();}
        }
    }
}
