package software.pipas.oprecox.modules.network;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.LobbyClient;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 05-Sep-17.
 */

public class ClientService extends IntentService
{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int time;
    private BroadcastReceiver broadcastReceiver;

    public ClientService() {super("TCPCommsService");}

    public ClientService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.time = getResources().getInteger(R.integer.TIME_FOR_ACCEPT_LIMIT);
        this.startBroadcastReceiver();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //CONNECTING
        InetSocketAddress inetSocketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S005_INETSOCKETADDRESS));

        if (!this.connect(inetSocketAddress))
        {
            this.terminate();
            return;
        }

        //INITIALIZING READER
        this.startBufferedReader();
        if (this.in == null)
        {
            this.terminate();
            return;
        }

        //INITIALIZING
        this.startPrintWriter();
        if (this.out == null)
        {
            this.terminate();
            return;
        }

        //RECEIVING PROCESS
        while (!socket.isClosed())
        {
            String str = this.receive();
            if(str == null)
                this.terminate();
            else
                this.sendMessage(str);
        }

    }

    public void sendResponse(ResponseType responseType)
    {
        Intent intent = new Intent(getResources().getString(R.string.S001));
        intent.putExtra(getResources().getString(R.string.S001_RESPONSE), responseType.toString());
        sendBroadcast(intent);

        Intent intent1 = new Intent(getResources().getString(R.string.S006));
        intent1.putExtra(getResources().getString(R.string.S006_RESPONSE), responseType.toString());
        sendBroadcast(intent1);
    }

    public void sendMessage(final String str)
    {
        Intent intent1 = new Intent(getResources().getString(R.string.S001));
        intent1.putExtra(getResources().getString(R.string.S001_MESSAGE), str);
        sendBroadcast(intent1);


        //failsafe in case LobbyClient hasnt started
        (new Thread()
        {
            public void run()
            {
                while (!LobbyClient.isLoaded()) {}
                Intent intent = new Intent(getResources().getString(R.string.S006));
                intent.putExtra(getResources().getString(R.string.S006_MESSAGE), str);
                sendBroadcast(intent);
            }
        }).start();

    }

    public void terminate()
    {
        try
        {
            this.socket.close();
            if(this.in != null) this.in.close();
            if(this.out != null) this.out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.stopSelf();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.terminate();
        this.sendResponse(ResponseType.CLOSED);
        this.unregisterReceiver();
    }

    private boolean connect(InetSocketAddress inetSocketAddress)
    {
        this.socket = new Socket();
        try
        {
            this.socket.connect(inetSocketAddress, this.time);
            sendResponse(ResponseType.CONNECTED);
            return true;
        }
        catch (SocketTimeoutException t)
        {
            sendResponse(ResponseType.TIMEOUT);
            stopSelf();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private void startBufferedReader()
    {
        try
        {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void startPrintWriter()
    {
        try
        {
            this.out = new PrintWriter(socket.getOutputStream(), false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleIntentReceived(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S005));
        registerReceiver(this.broadcastReceiver, filter);
    }

    private void unregisterReceiver()
    {
        unregisterReceiver(this.broadcastReceiver);
    }

    private String receive()
    {
        try
        {
            String str = this.in.readLine();
            return str;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //for receiving S005 stuff, from own app
    private void handleIntentReceived(Context context, Intent intent)
    {
        String str = intent.getExtras().getString(getString(R.string.S005_MESSAGE));
        Message msg = new Message(this.getApplicationContext(), str);

        if(msg.isValid() && msg.getMessageType().equals(MessageType.ID.toString()))
        {
            this.out.write(msg.getMessage() + "\n");
            this.out.flush();
        }

    }

}
