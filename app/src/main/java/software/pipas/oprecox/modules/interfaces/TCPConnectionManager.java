package software.pipas.oprecox.modules.interfaces;

import java.net.Socket;

import software.pipas.oprecox.modules.message.MessageType;
import software.pipas.oprecox.modules.message.ResponseType;

/**
 * Created by nuno_ on 04-Sep-17.
 */

public interface TCPConnectionManager
{
    void onConnectionCallback(Socket socket, ResponseType type, String message);
}
