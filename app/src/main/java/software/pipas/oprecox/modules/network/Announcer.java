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

import software.pipas.oprecox.R;
import software.pipas.oprecox.util.Util;


public class Announcer extends AsyncTask<Void, Void, Void>
{
    private Context context;
    private boolean valid;
    private boolean closed;
    private DatagramSocket socket;
    private DatagramPacket packet;


    public Announcer(Context context)
    {

        this.initialize(context);
    }

    @Override
    protected Void doInBackground(Void... args)
    {
        this.startLoop();
        return null;
    }


    private void initialize(Context context)
    {
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
            return false;
        }

    }

    private boolean createPacket()
    {
        //TO CHANGE TO MESSAGE CLASS
        String app_name = this.context.getString(R.string.network_app_name);
        String version = Util.getAppVersion(this.context);
        String announce = this.context.getString(R.string.network_announce);

        String text = app_name + " " + version + " " + announce;

        try
        {
            this.packet = new DatagramPacket(text.getBytes(), 0, text.length());

            //setting default broadcast ip, later to be changed programmatically
            this.packet.setSocketAddress(new InetSocketAddress(
                            InetAddress.getByName(this.context.getString(R.string.address_send)),
                            this.context.getResources().getInteger(R.integer.network_port)));


            return true;
        }
        catch (Exception e)
        {
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
