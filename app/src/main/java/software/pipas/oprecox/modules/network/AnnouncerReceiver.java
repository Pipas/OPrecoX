package software.pipas.oprecox.modules.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;

public class AnnouncerReceiver extends Thread
{
    private Context context;
    private Activity activity;
    private DatagramSocket socket;
    private Boolean valid;
    private Boolean closed;


    public AnnouncerReceiver(Context context, Activity activity)
    {
        this.initialize(context, activity);
    }

    private void initialize(Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
        this.closed = false;
        this.valid = (this.socketCreate());

    }


    @Override
    public void run()
    {
        this.startLoop();
    }


    private boolean socketCreate()
    {
        try
        {
            this.socket = new DatagramSocket(this.context.getResources().getInteger(R.integer.network_port));
            socket.setBroadcast(true);
            return true;
        }
        catch (SocketException s)
        {
            s.printStackTrace();
            return false;
        }
    }

    private void startLoop()
    {
        byte[] array = new byte[this.context.getResources().getInteger(R.integer.MAX_SIZE)];
        DatagramPacket packet = new DatagramPacket(array,0, array.length);

        while(!this.closed)
        {
            try
            {
                this.socket.receive(packet);
                ((MultiplayerClass) this.activity).registerReceived(packet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    public boolean isValid()
    {
        return this.valid;
    }

    public boolean isClosed()
    {
        return this.closed;
    }

    public void close()
    {
        this.closed = true;
        this.socket.close();
    }

    //exclusive for Invite Activity to send
    public void sendInvite(DatagramPacket packet)
    {
        try {this.socket.send(packet);}
        catch (IOException e) {e.printStackTrace();}
    }
}
