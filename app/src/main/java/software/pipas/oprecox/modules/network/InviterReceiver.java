package software.pipas.oprecox.modules.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customActivities.MultiplayerClass;
import software.pipas.oprecox.modules.dataType.Invite;

/**
 * Created by nuno_ on 18-Aug-17.
 */

public class InviterReceiver extends AsyncTask<Void, Void, Void>
{
    private Context context;
    private Activity activity;
    private DatagramSocket socket;

    public InviterReceiver(Context context, Activity activity, DatagramSocket socket)
    {
        this.context = context;
        this.activity = activity;
        this.socket = socket;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        byte[] array = new byte[this.context.getResources().getInteger(R.integer.MAX_SIZE)];
        DatagramPacket packet = new DatagramPacket(array,0, array.length);

        while (!this.socket.isClosed())
        {
            try
            {
                this.socket.receive(packet);
                ((MultiplayerClass) this.activity).registerReceived(packet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if(this.socket.isClosed()) break;
            }
        }

        return null;
    }
}
