package software.pipas.oprecox.modules.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 05-Sep-17.
 */

public class TCPCommsService extends IntentService
{

    private Socket socket;
    private int time;

    public TCPCommsService() {super("TCPCommsService");}

    public TCPCommsService(String name) {super(name);}

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.time = getResources().getInteger(R.integer.TIME_FOR_ACCEPT_LIMIT);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {

        //CONNECTING
        InetSocketAddress inetSocketAddress = (InetSocketAddress) intent.getExtras().getSerializable(getResources().getString(R.string.S005_INETSOCKETADDRESS));
        this.socket = new Socket();
        try
        {
            this.socket.connect(inetSocketAddress, this.time);
            sendResponse(ResponseType.CONNECTED);
        }
        catch (SocketTimeoutException t)
        {
            sendResponse(ResponseType.TIMEOUT);
            stopSelf();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //INITIALIZING READER
        BufferedReader in;
        try
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            this.sendResponse(ResponseType.CLOSED);
            e.printStackTrace();
            stopSelf();
            return;
        }


        //RECEIVING PROCESS
        while (!socket.isClosed())
        {
            try
            {
                String str = in.readLine();
                if(str == null)
                {
                    this.sendResponse(ResponseType.CLOSED);
                    break;
                }
                else
                {
                    this.sendMessage(str);
                }

            }
            catch (IOException e)
            {
                this.sendResponse(ResponseType.CLOSED);
                e.printStackTrace();
                break;
            }
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

    public void sendMessage(String str)
    {
        Intent intent = new Intent(getResources().getString(R.string.S006));
        intent.putExtra(getResources().getString(R.string.S006_MESSAGE), str);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try {this.socket.close();}
        catch (IOException e) {e.printStackTrace();}
    }
}
