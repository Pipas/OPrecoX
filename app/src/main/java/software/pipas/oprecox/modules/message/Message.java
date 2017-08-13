package software.pipas.oprecox.modules.message;

import android.content.Context;
import android.util.Log;

import java.net.DatagramPacket;

import software.pipas.oprecox.R;

public class Message
{

    private Context context;

    private String appName;
    private String appVersion;
    private MessageType messageType;
    private String playerName;
    private String playerId;
    private String playerIconURI;

    private boolean valid;


    //Message Dual-Constructor
    //--------------------------------------------------
    public Message(Context context, DatagramPacket packet)
    {
        this.context = context;
        this.defaultInititalize();
        this.valid = this.initialize(packet);

    }

    public Message(Context context, String args[])
    {
        this.context = context;
        this.defaultInititalize();
        this.valid = this.initialize(args);
    }
    //--------------------------------------------------


    //Default Initializer
    //--------------------------------------------------
    public void defaultInititalize()
    {
        this.appName = null;
        this.appVersion = null;
        this.messageType = null;
        this.playerName = null;
        this.playerId = null;
        this.playerIconURI = null;
        this.valid = false;
    }
    //--------------------------------------------------


    //Message Dual-Initializers
    //--------------------------------------------------
    private boolean initialize(String[] args)
    {
        //initial header test
        if(args.length < 3 || !this.testAppName(args[0]) || !this.testAppVersion(args[1]) || !this.testType(args[2]))
        return false;

        //initialize by type
        return this.initializeByType(args);
    }

    private boolean initialize(DatagramPacket packet)
    {
        return false;
    }
    //--------------------------------------------------


    //Initialize by Type
    //--------------------------------------------------
    public boolean initializeByType(String[] args)
    {
        if(messageType.equals(MessageType.ANNOUNCE) && args.length == 6)
        {
            this.playerName = args[3];
            this.playerId = args[4];
            this.playerIconURI = args[5];
            return true;
        }
        else
        {
            return false;
        }

    }
    //--------------------------------------------------





    //Individual Testers and initializers
    //--------------------------------------------------
    public boolean testAppName(String appName)
    {
        this.appName = appName;
        return appName.equals(this.context.getString(R.string.network_app_name));
    }

    private boolean testAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
        return appVersion.matches("\\d");
    }

    private boolean testType(String messageType)
    {
        for(MessageType msgType : MessageType.values())
        {
            if(msgType.toString().equals(messageType))
            {
                this.messageType = msgType;
                return true;
            }
        }

        this.messageType = null;
        return false;
    }
    //--------------------------------------------------


    public boolean isValid() {return this.valid;}

    public String getMessage()
    {
        if(messageType.equals(MessageType.ANNOUNCE))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.playerName + " " + this.playerId + " " + this.playerIconURI);
        }

        else
        {
            return null;
        }
    }

    public static String[] messageSplit(String message)
    {
        return message.split("\\s");
    }
}
