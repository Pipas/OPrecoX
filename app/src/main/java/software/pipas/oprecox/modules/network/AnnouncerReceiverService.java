package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import software.pipas.oprecox.R;

/**
 * Created by nuno_ on 26-Aug-17.
 */

public class AnnouncerReceiverService extends IntentService
{
    private Context context;
    private DatagramSocket socket;
    private Boolean closed;

    public AnnouncerReceiverService() {super("AnnouncerReceiverService");}
    public AnnouncerReceiverService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.context = this.getApplicationContext();
        this.closed = false;
        this.socketCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        this.startLoop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.closed = true;
        this.socket.close();
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
                String temp = new String(packet.getData(), 0, packet.getLength());

                Intent intent = new Intent(getResources().getString(R.string.ANNOUNCER_PACKET_ACTION));
                intent.putExtra(getResources().getString(R.string.ANNOUNCER_PACKET_MSG), temp);
                intent.putExtra(getResources().getString(R.string.ANNOUNCER_PACKET_IP), packet.getAddress());

                sendBroadcast(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
