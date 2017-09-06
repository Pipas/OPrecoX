package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.modules.interfaces.TCPConnectionManager;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 31-Aug-17.
 */

public class Room extends IntentService implements TCPConnectionManager
{
    private BroadcastReceiver broadcastReceiver;
    private ServerSocket serverSocket;
    private boolean closed;
    private int time;

    private LinkedList<Socket> joined;
    private LinkedList<Player> playersDB;

    public Room() {super("Room");}
    public Room(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.joined = new LinkedList<>();
        this.playersDB = new LinkedList<>();
        this.time = getResources().getInteger(R.integer.TIME_LIMIT_FOR_ROOM_REFRESH);
        this.closed = false;
        this.initializeServerSocket();
        this.initializeBroadcastReceiver();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        while (!this.closed)
        {
            try
            {
                Socket socket = this.serverSocket.accept();
                this.joined.add(socket);
                startTCPPlayerHandler(socket);
            }
            catch (SocketException s)
            {
                this.closed = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.closed = true;
        this.terminateConnections();
        unregisterReceiver(this.broadcastReceiver);

        try {this.serverSocket.close();}
        catch (IOException e) {e.printStackTrace();}
    }

    public void terminateConnections()
    {
        for(Socket socket : this.joined)
        {
            try {socket.close();}
            catch (IOException e) {e.printStackTrace();}
        }
    }

    public void initializeBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleIntentReceived(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S004));
        registerReceiver(this.broadcastReceiver, filter);
    }

    public void initializeServerSocket()
    {
        try
        {
            this.serverSocket = new ServerSocket(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            stopSelf();
        }
    }

    //receiver for Room, S004 receive of Pre-Invites to send Invites
    public void handleIntentReceived(Context context, Intent intent)
    {
        String message = intent.getExtras().getString(getResources().getString(R.string.S004_MESSAGE));
        InetSocketAddress socketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S004_INETSOCKETADDRESS));
        Player remotePlayer = intent.getExtras().getParcelable(getResources().getString(R.string.S004_PLAYER));
        this.playersDB.add(remotePlayer);

        Message msg = new Message(this.getApplicationContext(), message);
        if(!msg.isValid()) return;

        String roomPort = this.requestPort();
        if(roomPort == null) return;

        String[] args = new String[8];
        args[0] = msg.getAppName();
        args[1] = msg.getAppVersion();
        args[2] = msg.getMessageType();
        args[3] = msg.getRoomName();
        args[4] = msg.getDisplayName();
        args[5] = msg.getDisplayName();
        args[6] = msg.getPlayerId();
        args[7] = roomPort;

        Message newMsg = new Message(this.getApplicationContext(), args);

        if(!newMsg.isValid()) return;


        Intent newIntent = new Intent(getResources().getString(R.string.S000));
        newIntent.putExtra(getResources().getString(R.string.S000_MESSAGE), newMsg.getMessage());
        newIntent.putExtra(getResources().getString(R.string.S000_INETSOCKETADDRESS), socketAddress);
        sendBroadcast(newIntent);
    }

    @Override
    //callback from every TCPPlayerHandler
    public void onConnectionCallback(Socket remotePlayerSocket, ResponseType type, String message)
    {
        if(type.equals(ResponseType.CLOSED))
        {
            this.joined.remove(remotePlayerSocket);
        }
        else if(type.equals(ResponseType.INFO))
        {
            //IMPLEMENT NEW MESSAGE DECODER AND FIGURE OUT NEW MESSAGE TYPES
        }

    }

    public String requestPort()
    {
        return Integer.toString(this.serverSocket.getLocalPort());

    }

    public void startTCPPlayerHandler(Socket socket)
    {
        TCPPlayerHandler tcpPlayerHandler = new TCPPlayerHandler(Room.this, socket);
        tcpPlayerHandler.start();
    }


    public void sleep()
    {
        try {Thread.sleep(this.time);}
        catch (InterruptedException e) {e.printStackTrace();}
    }



}
