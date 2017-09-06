package software.pipas.oprecox.modules.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import software.pipas.oprecox.modules.interfaces.TCPConnectionManager;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 04-Sep-17.
 */

public class TCPPlayerHandler extends Thread
{
    TCPConnectionManager tcpConnectionManager;
    Socket socket;

    public TCPPlayerHandler(TCPConnectionManager tcpConnectionManager, Socket socket)
    {
        this.tcpConnectionManager = tcpConnectionManager;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        BufferedReader in;
        try
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            this.sendClosedSignal();
            e.printStackTrace();
            return;
        }

        while (!socket.isClosed())
        {
            try
            {
                String str = in.readLine();
                if(str == null)
                {
                    this.sendClosedSignal();
                    break;
                }
                else
                {
                    this.sendResponse(str);
                }

            }
            catch (IOException e)
            {
                this.sendClosedSignal();
                e.printStackTrace();
                break;
            }
        }

    }

    private void sendClosedSignal()
    {
        this.tcpConnectionManager.onConnectionCallback(this.socket, ResponseType.CLOSED, "socket closed");
    }

    private void sendResponse(String str)
    {
        this.tcpConnectionManager.onConnectionCallback(this.socket, ResponseType.INFO, str);
    }
}
