package software.pipas.oprecox.modules.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by nuno_ on 18-Aug-17.
 */

public class InviterSender extends AsyncTask<Void, Void, Void>
{

    private Context context;
    private Activity activity;
    private DatagramPacket packet;
    private DatagramSocket socket;

    public InviterSender(Context context, Activity activity, DatagramPacket packet, DatagramSocket socket)
    {
        this.context = context;
        this.activity = activity;
        this.packet = packet;
        this.socket = socket;
    }


    @Override
    protected Void doInBackground(Void... params)
    {
        if(this.socket != null && !this.socket.isClosed())
        {
            try {this.socket.send(this.packet);}
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this.context, "Unable to Send Invite: Send", Toast.LENGTH_SHORT).show();}
        }
        else
        {
            Toast.makeText(this.context, "Unable to Send Invite: Socket", Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}
