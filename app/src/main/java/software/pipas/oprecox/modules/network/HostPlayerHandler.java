package software.pipas.oprecox.modules.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import software.pipas.oprecox.modules.interfaces.TCPConnectionManager;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 04-Sep-17.
 */

public class HostPlayerHandler extends Thread
{
    TCPConnectionManager tcpConnectionManager;
    Socket socket;
    BufferedReader in;

    public HostPlayerHandler(TCPConnectionManager tcpConnectionManager, Socket socket)
    {
        this.tcpConnectionManager = tcpConnectionManager;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        this.in = this.getBufferedReader();

        if(in == null)
        {
            this.terminate();
            return;
        }


        while (!this.socket.isClosed())
        {

            String str = receive();

            if(str == null)
            {
                this.terminate();
                break;
            }
            else
            {
                this.sendResponse(str);
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

    private void terminate()
    {
        this.sendClosedSignal();
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader getBufferedReader()
    {
        BufferedReader in;
        try
        {
            in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            return in;
        }
        catch (IOException e)
        {

            e.printStackTrace();
            return null;
        }
    }

    private PrintWriter getPrintWriter()
    {
        PrintWriter out;
        try
        {
            out = new PrintWriter(this.socket.getOutputStream(), false);
            return out;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private String receive()
    {
        try {
            String str = in.readLine();
            return str;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
