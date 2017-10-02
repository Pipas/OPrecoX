package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.LobbyClient;
import software.pipas.oprecox.modules.customThreads.PendingMessage;
import software.pipas.oprecox.modules.message.Message;
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
    private ArrayList<PendingMessage> pendingMessages;

    public ClientService() {super("TCPCommsService");}

    public ClientService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        Log.d("HANDLE","created CLIENT");
        super.onCreate();
        this.time = getResources().getInteger(R.integer.TIME_FOR_ACCEPT_LIMIT);
        this.startBroadcastReceiver();
        this.pendingMessages = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("HANDLE","started CLIENT");
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

        Intent intent2 = new Intent(getResources().getString(R.string.S008));
        intent2.putExtra(getResources().getString(R.string.S008_RESPONSE), responseType.toString());
        sendBroadcast(intent2);

        Intent intent3 = new Intent(getResources().getString(R.string.S009));
        intent3.putExtra(getResources().getString(R.string.S009_RESPONSE), responseType.toString());
        sendBroadcast(intent3);
    }

    public void sendMessage(final String str)
    {


        Intent intent1 = new Intent(getResources().getString(R.string.S001));
        intent1.putExtra(getResources().getString(R.string.S001_MESSAGE), str);
        sendBroadcast(intent1);
/*
        Intent intent = new Intent(getResources().getString(R.string.S006));
        intent.putExtra(getResources().getString(R.string.S006_MESSAGE), str);
        sendBroadcast(intent);
*/
        Intent intent2 = new Intent(getResources().getString(R.string.S008));
        intent2.putExtra(getResources().getString(R.string.S008_MESSAGE), str);
        sendBroadcast(intent2);


        //previous S006 comm, dont erase, might need it
        //failsafe in case LobbyClient hasnt started


        PendingMessage p = (new PendingMessage()
        {
            public void run()
            {
                while (!LobbyClient.isLoaded() && !closed) {}
                Intent intent = new Intent(getResources().getString(R.string.S006));
                intent.putExtra(getResources().getString(R.string.S006_MESSAGE), str);
                sendBroadcast(intent);
            }
        });

        p.start();
        this.pendingMessages.add(p);
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
        this.terminatePending();
    }

    private void terminatePending()
    {
        for(PendingMessage pendingMessage : this.pendingMessages)
        {
            pendingMessage.terminate();
        }
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
    private void handleIntentReceived(Context context, final Intent intent)
    {
        (new Thread()
        {
            public void run()
            {
                String str = intent.getExtras().getString(getString(R.string.S005_MESSAGE));
                Message msg = new Message(getApplicationContext(), str);

                if(msg.isValid())
                {
                    out.write(msg.getMessage() + "\n");
                    out.flush();
                }
            }
        }).start();

    }

}
