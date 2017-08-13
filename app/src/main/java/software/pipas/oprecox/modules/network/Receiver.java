package software.pipas.oprecox.modules.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import software.pipas.oprecox.R;

/**
 * Created by nuno_ on 19-Jul-17.
 */

public class Receiver extends AsyncTask<Void, Void, Void>
{
    private Context context;
    private String myGamerId;
    private DatagramSocket socket;
    private Boolean valid;
    private Boolean closed;


    public Receiver(Context context, String myGamerId)
    {
        this.initialize(context, myGamerId);
    }

    private void initialize(Context context, String myGamerId)
    {
        this.context = context;
        this.myGamerId = myGamerId;
        this.closed = false;
        this.valid = (this.socketCreate());

    }


    @Override
    protected Void doInBackground(Void... params)
    {
        this.startLoop();
        return null;
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
                String str = new String(packet.getData(), 0, packet.getLength());
                Log.d("RECEIVED", str);
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
}
