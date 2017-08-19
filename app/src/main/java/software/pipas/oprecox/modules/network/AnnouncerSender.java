package software.pipas.oprecox.modules.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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


public class AnnouncerSender extends AsyncTask<Void, Void, Void>
{
    private Context context;
    private String name;
    private String displayName;
    private String playerId;
    private int invitePort;
    private boolean valid;
    private boolean closed;
    private DatagramSocket socket;
    private DatagramPacket packet;


    public AnnouncerSender(Context context, String name, String displayName, String playerId, int invitePort)
    {
        this.initialize(context, name,displayName, playerId, invitePort);
    }

    @Override
    protected Void doInBackground(Void... args)
    {
        this.startLoop();
        return null;
    }


    private void initialize(Context context, String name, String displayName, String playerId, int invitePort)
    {
        this.name = name;
        this.displayName = displayName;
        this.playerId = playerId;
        this.invitePort = invitePort;
        this.context = context;
        this.closed = false;
        this.valid = (this.createSocket() && this.createPacket());
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

    public boolean isValid()
    {
        return this.valid;
    }

    public void close()
    {
        this.closed = true;
    }


}
