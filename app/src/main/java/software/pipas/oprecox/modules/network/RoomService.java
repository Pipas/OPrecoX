package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.modules.interfaces.TCPConnectionManager;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 31-Aug-17.
 */

public class RoomService extends IntentService implements TCPConnectionManager
{
    private BroadcastReceiver broadcastReceiver;
    private ServerSocket serverSocket;
    private boolean closed;
    private int time;

    private LinkedList<Socket> pending;
    private HashMap<Player, Socket> joined;
    private LinkedList<Player> playersDB;

    public RoomService() {super("Room");}
    public RoomService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.pending = new LinkedList<>();
        this.joined = new HashMap<>();
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
                this.pending.add(socket);
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
        for(Socket socket : this.joined.values())
        {
            try {socket.close();}
            catch (IOException e) {e.printStackTrace();}
        }

        for(Socket socket : this.pending)
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
    //callback from every HostPlayerHandler
    public void onConnectionCallback(Socket remotePlayerSocket, ResponseType type, String message)
    {
        if(type.equals(ResponseType.CLOSED))
        {
            this.joined.remove(remotePlayerSocket);
            this.pending.remove(remotePlayerSocket);
            this.removePlayerFromListAndSendToAll(remotePlayerSocket);
        }
        else if(type.equals(ResponseType.INFO))
        {
            Message msg = new Message(this.getApplicationContext(), message);
            if(msg.isValid())
            {
                //if receives a player joined ID
                if(msg.getMessageType().equals(MessageType.ID.toString()))
                {
                    String id = msg.getPlayerId();
                    Player dummyPlayer = new Player(id);

                    for(Player player1 : this.playersDB)
                    {
                        if(player1.equals(dummyPlayer))
                        {
                            this.joined.put(player1, remotePlayerSocket);
                            this.addPlayerToListAndSendToAll(player1, remotePlayerSocket);
                            break;
                        }
                    }
                }

                //other messages...

            }
        }

    }

    public String requestPort()
    {
        return Integer.toString(this.serverSocket.getLocalPort());

    }

    public void startTCPPlayerHandler(Socket socket)
    {
        HostPlayerHandler hostPlayerHandler = new HostPlayerHandler(RoomService.this, socket);
        hostPlayerHandler.start();
        this.identifyPlayer(socket);
    }

    private void identifyPlayer(Socket socket)
    {
        String[] args = new String[3];
        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.REQUESTID.toString();

        Message msg = new Message(this.getApplicationContext(), args);
        if(!msg.isValid()) return;

        try
        {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
            out.write(msg.getMessage() + "\n");
            out.flush();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    private void addPlayerToListAndSendToAll(Player player, Socket socket)
    {
        //sending to me
        Intent intent = new Intent(getString(R.string.S007));
        intent.putExtra(getString(R.string.S007_ADDPLAYERLIST), player);
        sendBroadcast(intent);

        //sending new player to everyone
        String playerName = player.getName();
        String displayName = player.getDisplayName();
        String playerID = player.getPlayerID();

        String[] args = new String[6];

        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.ADDPLAYERLIST.toString();
        args[3] = playerName;
        args[4] = displayName;
        args[5] = playerID;

        Message msg = new Message(this.getApplicationContext(), args);

        if(!msg.isValid()) return;

        this.sendBroadcastToClients(msg);

        //must send joined players, except own 'player' to 'socket'
        this.sendRestToPlayer(player, socket);

    }

    private void removePlayerFromListAndSendToAll(Socket socket)
    {
        for(HashMap.Entry<Player, Socket> entry : this.joined.entrySet())
        {
            if(entry.getValue().equals(socket))
            {
                Player player = entry.getKey();

                Intent intent = new Intent(getString(R.string.S007));
                intent.putExtra(getString(R.string.S007_REMOVEPLAYERLIST), player);
                sendBroadcast(intent);

                String[] args = new String[4];

                args[0] = this.getString(R.string.network_app_name);
                args[1] = Integer.toString(BuildConfig.VERSION_CODE);
                args[2] = MessageType.REMOVEPLAYERLIST.toString();
                args[3] = player.getPlayerID();

                Message msg = new Message(this.getApplicationContext(), args);

                if(!msg.isValid()) return;

                this.sendBroadcastToClients(msg);
            }
        }
    }

    private void sendRestToPlayer(Player playerToSend, Socket socket)
    {
        for(HashMap.Entry<Player, Socket> entry : this.joined.entrySet())
        {
            Player player = entry.getKey();

            if(playerToSend.equals(player)) continue;

            String playerName = player.getName();
            String displayName = player.getDisplayName();
            String playerID = player.getPlayerID();

            String[] args = new String[6];

            args[0] = this.getString(R.string.network_app_name);
            args[1] = Integer.toString(BuildConfig.VERSION_CODE);
            args[2] = MessageType.ADDPLAYERLIST.toString();
            args[3] = playerName;
            args[4] = displayName;
            args[5] = playerID;

            Message msg = new Message(this.getApplicationContext(), args);

            if(!msg.isValid()) return;

            this.singleSend(socket, msg);
        }
    }

    private void singleSend(final Socket socket, final Message message)
    {
        (new Thread() {
            public void run()
            {
                try
                {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
                    out.write(message.getMessage() + "\n");
                    out.flush();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    private void sendBroadcastToClients(final Message message)
    {
        for(final Socket socket : this.joined.values())
        {
            (new Thread() {
                public void run()
                {
                    try
                    {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
                        out.write(message.getMessage() + "\n");
                        out.flush();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
            }).start();
        }
    }


}
