package software.pipas.oprecox.modules.customThreads;

/**
 * Created by nuno_ on 29-Sep-17.
 */

public class PendingMessage extends Thread
{
    protected boolean closed;

    public PendingMessage()
    {
        closed = false;
    }

    public void terminate()
    {
        closed = true;
    }
}
