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
import java.util.concurrent.ConcurrentHashMap;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.Invite;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.modules.interfaces.OnTCPConnectionManager;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 31-Aug-17.
 */

public class RoomService extends IntentService implements OnTCPConnectionManager
{
    private BroadcastReceiver broadcastReceiver;
    private ServerSocket serverSocket;
    private boolean closed;
    private int time;

    private LinkedList<Socket> pending;
    private ConcurrentHashMap<Player, Socket> joined;
    private ConcurrentHashMap<Player, Socket> pendingLoaded;
    private LinkedList<Player> playersDB;

    private ConcurrentHashMap<Player, Socket> reservedPlayers;
    private LinkedList<Player> readyPlayers;
    private boolean hostReady;

    public RoomService() {super("Room");}
    public RoomService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        Log.d("HANDLE", "created ROOM");
        super.onCreate();
        this.pending = new LinkedList<>();
        this.joined = new ConcurrentHashMap<>();
        this.pendingLoaded = new ConcurrentHashMap<>();
        this.playersDB = new LinkedList<>();
        this.time = getResources().getInteger(R.integer.TIME_LIMIT_FOR_ROOM_REFRESH);
        this.closed = false;
        this.initializeServerSocket();
        this.initializeBroadcastReceiver();
        this.hostReady = false;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        Log.d("HANDLE", "started ROOM");
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

        for(Socket socket : this.pendingLoaded.values())
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

    private void reserveJoinedPlayers()
    {
        this.reservedPlayers = this.joined;
        this.readyPlayers = new LinkedList<>();
    }

    //receiver for Room, S004 receive of Pre-Invites to send Invites
    public void handleIntentReceived(Context context, Intent intent)
    {
        String message = intent.getExtras().getString(getResources().getString(R.string.S004_MESSAGE));
        InetSocketAddress socketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S004_INETSOCKETADDRESS));
        Player remotePlayer = intent.getExtras().getParcelable(getResources().getString(R.string.S004_PLAYER));

        if(remotePlayer != null) this.playersDB.add(remotePlayer);

        if(message != null && socketAddress != null)
        {
            Message msg = new Message(this.getApplicationContext(), message);
            if (!msg.isValid()) return;

            String roomPort = this.requestPort();
            if (roomPort == null) return;

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

            if (!newMsg.isValid()) return;


            Intent newIntent = new Intent(getResources().getString(R.string.S000));
            newIntent.putExtra(getResources().getString(R.string.S000_MESSAGE), newMsg.getMessage());
            newIntent.putExtra(getResources().getString(R.string.S000_INETSOCKETADDRESS), socketAddress);
            sendBroadcast(newIntent);
        }

        Player playerLoaded = intent.getExtras().getParcelable(getString(R.string.S004_LOADEDPLAYER));
        if(playerLoaded != null)
        {
            this.playersDB.add(playerLoaded);

            for(Player player : this.pendingLoaded.keySet())
            {
                if(player.equals(playerLoaded))
                {
                    for(Player playerJoined : this.joined.keySet())
                    {
                        //failsafe in case the same player joins two times
                        if(playerJoined.equals(playerLoaded)) {return;}
                    }

                    Socket socket = this.pendingLoaded.get(player);
                    playerLoaded.updatePlayerAddress(socket.getInetAddress());
                    playerLoaded.updatePlayerInvitePort(socket.getPort());

                    this.joined.put(playerLoaded, socket);
                    this.addPlayerToListAndSendToAll(playerLoaded, socket);
                    this.pendingLoaded.remove(player);
                    break;
                }
            }
        }

        String roomName = intent.getExtras().getString(getString(R.string.S004_ACTUALIZEROOMNAME));
        if (roomName != null)
        {
            String[] args = new String[4];
            args[0] = this.getString(R.string.network_app_name);
            args[1] = Integer.toString(BuildConfig.VERSION_CODE);
            args[2] = MessageType.ACTUALIZEROOMNAME.toString();
            args[3] = roomName;

            Message messageRoom = new Message(this, args);

            if(!messageRoom.isValid()) { Log.d("ROOM_NAME", "not valid" + "\n" + messageRoom.getMessage()); return;}

            Log.d("ROOM_NAME", messageRoom.getMessage());

            this.sendBroadcastToClients(messageRoom);
            return;
        }

        String urls = intent.getExtras().getString(getString(R.string.S004_GAMEURLS));
        if(urls != null)
        {
            Message msg = new Message(this.getApplicationContext(), urls);
            if(msg.isValid())
            {
                this.sendBroadcastToClients(msg);
                this.reserveJoinedPlayers();
            }
            return;
        }

        String hostReady = intent.getExtras().getString(getString(R.string.S004_HOSTREADY));
        if(hostReady != null)
        {
            this.hostReady = true;
            if(this.checkReadyAndJoined())
            {
                this.sendGameStartToPlayersAndHost(this.reservedPlayers);
            }
            return;
        }

        String exitGameActivity = intent.getExtras().getString(getString(R.string.S004_EXITGAMEACTIVITY));
        if(exitGameActivity != null) {
            Message msg = new Message(this.getApplicationContext(), exitGameActivity);
            if (!msg.isValid()) return;
            for (Socket socket : this.reservedPlayers.values()) {
                this.singleSend(socket, msg);
            }
            resetStartGame();
            return;
        }

        String answer = intent.getExtras().getString(getString(R.string.S004_ROUNDANSWER));
        if (answer != null)
        {
            Message msg = new Message(this.getApplicationContext(), answer);
            this.hostAnswer(msg);
            return;
        }

        String nextRound = intent.getExtras().getString(getString(R.string.S004_NEXTROUND));
        if(nextRound != null)
        {
            String[] args = new String[3];
            args[0] = this.getString(R.string.network_app_name);
            args[1] = Integer.toString(BuildConfig.VERSION_CODE);
            args[2] = MessageType.NEXTROUND.toString();

            Message msg = new Message(this.getApplicationContext(), args);

            if(!msg.isValid()) {Log.d("ROOM_DEBUG", "nextround not valid"); return;}

            for(Socket socket : this.reservedPlayers.values())
            {
                this.singleSend(socket, msg);
            }
        }


    }

    @Override
    //callback from every HostPlayerHandler
    public void onConnectionCallback(Socket remotePlayerSocket, ResponseType type, String message)
    {
        if(type.equals(ResponseType.CLOSED))
        {
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
                    String displayName = msg.getDisplayName();

                    Player dummyPlayer = new Player(id);
                    dummyPlayer.updatePlayerAnnouncedTime(System.currentTimeMillis());
                    dummyPlayer.updatePlayerInvitePort(-1);
                    dummyPlayer.updatePlayerDisplayName(displayName);

                    for(Player player1 : this.playersDB)
                    {
                        if(player1.equals(dummyPlayer))
                        {
                            this.joined.put(player1, remotePlayerSocket);
                            this.addPlayerToListAndSendToAll(player1, remotePlayerSocket);
                            break;
                        }
                    }

                    Log.d("MY_IP_DEBUG", "putting pending");
                    this.pendingLoaded.put(dummyPlayer, remotePlayerSocket);
                    this.sendToActivityToLoad(dummyPlayer);
                }
                else if(msg.getMessageType().equals(MessageType.READY.toString()))
                {
                    Player playerTemp = new Player(msg.getPlayerId());
                    this.readyPlayers.remove(playerTemp);
                    this.readyPlayers.add(playerTemp);

                    if(this.checkReadyAndJoined())
                    {
                        this.sendGameStartToPlayersAndHost(this.reservedPlayers);
                    }
                }
                else if(msg.getMessageType().equals(MessageType.ROUNDSCORE.toString()))
                {
                    this.clientAnswer(msg);
                }
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

                this.joined.remove(player);
            }
        }

    if(this.reservedPlayers != null)
    {
        for (HashMap.Entry<Player, Socket> entry : this.reservedPlayers.entrySet()) {
            if (entry.getValue().equals(socket)) {
                Player player = entry.getKey();
                this.reservedPlayers.remove(player);
            }
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

    private void sendToActivityToLoad(Player dummyPlayer)
    {
        //chosing which activity it loads from
        if(Invite.isClosed())
        {
            Intent intent = new Intent(getString(R.string.S007));
            intent.putExtra(getString(R.string.S007_REQUESPLAYERLOADER), dummyPlayer);
            sendBroadcast(intent);
        }
        else
        {
            Intent intent = new Intent(getString(R.string.S002));
            intent.putExtra(getString(R.string.S002_REQUESPLAYERLOADER), dummyPlayer);
            sendBroadcast(intent);
        }

    }

    private void sendGameStartToPlayersAndHost(ConcurrentHashMap<Player, Socket> readyPlayers)
    {
        startGame();
        Intent intent = new Intent(getString(R.string.S007));
        intent.putExtra(getString(R.string.S007_STARTGAME), "");
        sendBroadcast(intent);

        String[] args = new String[3];

        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.STARTGAME.toString();

        Message msg = new Message(this.getApplicationContext(), args);

        if (!msg.isValid()) return;

        for(Socket socket : readyPlayers.values())
        {
            this.singleSend(socket, msg);
        }

    }

    private void resetStartGame()
    {
        this.readyPlayers = null;
        this.reservedPlayers = null;
        this.hostReady = false;
    }

    private boolean checkReadyAndJoined()
    {
        if(this.readyPlayers != null && this.reservedPlayers != null)
        {
            if(!this.hostReady) return false;

            int size1 = this.readyPlayers.size();
            int size2 = this.reservedPlayers.size();

            Log.d("READY_DEBUG", "number: " + size1 + " " + size2);

            if(size1 != size2) return false;

            Player player1 = this.readyPlayers.get(0);

            for(Player player : this.reservedPlayers.keySet())
            {
                int i = this.readyPlayers.indexOf(player);

                if(i < 0) return false;
            }


            return true;

        }
        else return false;
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

    //============================================================================================
    //gaming part
    //declaration temp
    private boolean gaming;
    private boolean hostAnswer;
    private LinkedList<Player> roundAnswers;

    private void clientAnswer(Message message)
    {
        Player player = new Player(message.getPlayerId());
        this.roundAnswers.add(player);
        if(this.verifyAnswersToContinue()) this.waitToAfter();
    }

    private void hostAnswer(Message message)
    {
        this.hostAnswer = true;
        if(this.verifyAnswersToContinue()) this.waitToAfter();
    }

    private void startGame()
    {
        this.gaming = true;
        this.resetRound();
    }

    private void stopGame()
    {
        this.gaming = false;
        this.resetRound();
        this.roundAnswers = null;
    }

    private void resetRound()
    {
        this.roundAnswers = new LinkedList<>();
        this.hostAnswer = false;
    }

    private void setHostAnswer(boolean answer)
    {
        this.hostAnswer = answer;
    }

    //to verify if everyone can continue
    private boolean verifyAnswersToContinue()
    {
        if((this.hostAnswer && this.roundAnswers.size() == 0) || (!this.hostAnswer && this.roundAnswers.size() == 1))
        {
            this.startTimer();
        }

        if(hostAnswer && roundAnswers.size() == this.reservedPlayers.size())
            return true;
        else
            return false;
    }

    private void waitToAfter()
    {
        this.resetRound();

        String[] args = new String[3];

        args[0] = this.getString(R.string.network_app_name);
        args[1] = Integer.toString(BuildConfig.VERSION_CODE);
        args[2] = MessageType.CONTINUEGAME.toString();

        Message msg = new Message(this.getApplicationContext(), args);

        if(!msg.isValid()) {Log.d("ROOM_DEBUG", "next round message not valid"); return;}

        Intent intent = new Intent(getString(R.string.S008));
        intent.putExtra(getString(R.string.S008_MESSAGE), msg.getMessage());
        sendBroadcast(intent);

        for(Socket socket : this.reservedPlayers.values())
        {
            this.singleSend(socket, msg);
        }
    }

    private void startTimer() {}

}
