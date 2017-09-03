package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.message.Message;
import software.pipas.oprecox.modules.message.MessageType;

/**
 * UDPCommsService
 * Uses one DatagramSocket bound to one port, to send and receive broadcastable.
 * Uses one BroadcastReceiver to handle system requests, or to send.
 * Two jobs: Send and Receive
 *
 * First Job: Constant receiving packets. Upon reception tries to handle. Checks if is a valid Message.
 * Case Positive, uses the broadcast receiver to Ship an intent with the message and socketAddress to the system,
 * with the Respective SXXX service identifier.
 *
 * Second Job: Constant receiving intents from BroadcastReceiver. Upon reception, converts the message to packet with
 * respective socketAddress and uses the socket to send.
 */

public class UDPCommsService extends IntentService
{

    private BroadcastReceiver broadcastReceiver;
    private DatagramSocket socket;

    public UDPCommsService() {super("UDPCommsService");}

    public UDPCommsService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.initializeSocket();
        this.initializeBroadcastReceiver();
    }



    @Override
    //TO RECEIVE PACKET FROM DATAGRAMSOCKET
    protected void onHandleIntent(@Nullable Intent intent)
    {
        byte[] buff = new byte[getResources().getInteger(R.integer.MAX_SIZE)];
        DatagramPacket packet = new DatagramPacket(buff, 0, buff.length);

        while (!this.socket.isClosed())
        {
            try
            {
                this.socket.receive(packet);
                this.handlePacketReceived(new String(packet.getData(),0,packet.getLength()), new InetSocketAddress(packet.getAddress(), packet.getPort()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //TO SEND PACKET FROM DATAGRAMSOCKET
    public void handleIntentReceived(Context context, Intent intent)
    {
        String str = intent.getExtras().getString(getResources().getString(R.string.S000_MESSAGE));
        InetSocketAddress address = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S000_INETSOCKETADDRESS));
        DatagramPacket packet = new DatagramPacket(str.getBytes(),0,str.getBytes().length);
        packet.setAddress(address.getAddress());
        packet.setPort(address.getPort());
        this.sendPacket(packet);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.closeBroadcastReceiver();
        this.closeSocket();
    }



    /**
     * Broacast Receiver stuff
     */
    public void initializeBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleIntentReceived(context, intent);}
        };
        IntentFilter filter = new IntentFilter(getResources().getString(R.string.S000));
        registerReceiver(this.broadcastReceiver, filter);
    }

    public void closeBroadcastReceiver()
    {
        unregisterReceiver(this.broadcastReceiver);
    }
    /** end */



    /**
     * Socket stuff
     */
    public boolean initializeSocket()
    {
        try
        {
            this.socket = new DatagramSocket(getResources().getInteger(R.integer.network_port));
            this.socket.setBroadcast(true);
            return true;
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void sendPacket(DatagramPacket packet)
    {
        final DatagramPacket newPacket = packet;

        (new Thread()
        {
            public void run()
            {
                try {socket.send(newPacket);}
                catch (IOException e) {e.printStackTrace();}
            }
        }).start();
    }

    public void closeSocket()
    {
        this.socket.close();
    }

    /**
     * Packet Handlers
     */
    public void handlePacketReceived(String message, InetSocketAddress socketAddress)
    {
        Message msg = new Message(this.getApplicationContext(), message);

        if(!msg.isValid()) return;

        if(msg.getMessageType().equals(MessageType.ANNOUNCE.toString()))
        {
            Intent intent  = new Intent(getResources().getString(R.string.S002));
            intent.putExtra(getResources().getString(R.string.S002_MESSAGE), message);
            intent.putExtra(getResources().getString(R.string.S002_INETSOCKETADDRESS), socketAddress);
            sendIntent(intent);
        }
        else if(msg.getMessageType().equals(MessageType.INVITE.toString()))
        {
            Intent intent  = new Intent(getResources().getString(R.string.S001));
            intent.putExtra(getResources().getString(R.string.S001_MESSAGE), message);
            intent.putExtra(getResources().getString(R.string.S001_INETSOCKETADDRESS), socketAddress);
            sendIntent(intent);
        }
    }

    public void sendIntent(Intent intent)
    {
        sendBroadcast(intent);
    }


}
