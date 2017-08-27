package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import software.pipas.oprecox.R;

/**
 * Created by nuno_ on 27-Aug-17.
 */

public class InviterReceiverService extends IntentService
{
    private Context context;
    private DatagramSocket socket;

    public InviterReceiverService() {
        super("InviterReceiverService");
    }
    public InviterReceiverService(String name) {
        super(name);
    }

    @Override
    public void onCreate()
    {
        Log.d("INVITER_RECEIVER", "created");
        super.onCreate();
        this.context = getApplicationContext();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        byte[] array = new byte[this.context.getResources().getInteger(R.integer.MAX_SIZE)];
        DatagramPacket packet = new DatagramPacket(array,0, array.length);

        int port = intent.getExtras().getInt(getResources().getString(R.string.INVITER_RECEIVER_PORT));
        try {this.socket = new DatagramSocket(port);}
        catch (SocketException e) {e.printStackTrace();Log.d("INVITER_RECEIVER", "failed to bind");return;}

        while (!this.socket.isClosed())
        {
            try
            {
                this.socket.receive(packet);

                String temp = new String(packet.getData(), 0, packet.getLength());

                Intent newIntent = new Intent(getResources().getString(R.string.INVITER_RECEIVER_ACTION));
                newIntent.putExtra(getResources().getString(R.string.INVITER_RECEIVER_MSG), temp);
                newIntent.putExtra(getResources().getString(R.string.INVITER_RECEIVER_IP), packet.getAddress());

                sendBroadcast(newIntent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if(this.socket.isClosed()) break;
            }
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        this.socket.close();
    }
}
