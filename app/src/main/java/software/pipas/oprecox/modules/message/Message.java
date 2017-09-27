package software.pipas.oprecox.modules.message;

import android.content.Context;

import java.net.DatagramPacket;
import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.util.Util;

public class Message
{

    private Context context;

    private String appName;
    private String appVersion;
    private MessageType messageType;
    private String name;
    private String displayName;
    private String playerId;
    private String roomName;
    private String roomPort;
    private int numberOfUrls;
    private ArrayList<String> urlsArrayList;
    private int totalScore;
    private int roundAnswer;

    private boolean valid;


    //Message Dual-Constructor
    //--------------------------------------------------
    public Message(Context context, DatagramPacket packet)
    {
        this.context = context;
        this.defaultInititalize();
        this.valid = this.initialize(packet);

    }

    public Message(Context context, String str)
    {
        this.context = context;
        this.defaultInititalize();
        this.valid = this.initialize(Message.messageSplit(str));
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
        this.name = null;
        this.displayName = null;
        this.playerId = null;
        this.valid = false;
        this.roomName = null;
        this.roomPort = null;
        this.numberOfUrls = -1;
        this.urlsArrayList = null;
        this.totalScore = -1;
        this.roundAnswer = -1;
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

        //retrieve packet message
        String msg = new String(packet.getData(), 0, packet.getLength());
        String[] args = messageSplit(msg);

        return initialize(args);
    }
    //--------------------------------------------------


    //Initialize by Type
    //--------------------------------------------------
    public boolean initializeByType(String[] args)
    {
        if(messageType.equals(MessageType.ANNOUNCE) && args.length == 6)
        {
            this.name = args[3];
            this.displayName = args[4];
            this.playerId = args[5];
            return true;
        }
        else if(messageType.equals(MessageType.INVITE) && args.length == 8)
        {
            this.roomName = args[3];
            this.displayName = args[4];
            this.name = args[5];
            this.playerId = args[6];
            this.roomPort = args[7];
            return true;
        }
        else if(messageType.equals(MessageType.REQUESTID) && args.length == 3)
        {
            return true;
        }
        else if(messageType.equals(MessageType.ID) && args.length == 5)
        {
            this.playerId = args[3];
            this.displayName = Util.substituteSpace(args[4]);
            return true;
        }
        else if(messageType.equals(MessageType.ADDPLAYERLIST) && args.length == 6)
        {
            this.name = args[3];
            this.displayName = args[4];
            this.playerId = args[5];
            return true;
        }
        else if(messageType.equals(MessageType.REMOVEPLAYERLIST) && args.length == 4)
        {
            this.playerId = args[3];
            return true;
        }
        else if(messageType.equals(MessageType.ACTUALIZEROOMNAME) && args.length == 4)
        {
            this.roomName = args[3];
            return true;
        }
        else if(messageType.equals(MessageType.GAMEURLS) && (args.length == 9 || args.length == 14 || args.length == 24))
        {
            try {this.numberOfUrls = Integer.parseInt(args[3]);}
            catch (NumberFormatException n) {return false;}

            int count = 3 + 1 + this.numberOfUrls; //3 for first three mandatory arguments, 1 for numberourls, numberofurls for number of urls
            if(count != args.length) return false;

            this.urlsArrayList = new ArrayList<>();

            for(int i = 4; i < args.length; i++)
            {
                this.urlsArrayList.add(args[i]);
            }

            if(urlsArrayList.size() != this.numberOfUrls) return false;

            return true;
        }
        else if(messageType.equals(MessageType.READY) && args.length == 4)
        {
            this.playerId = args[3];
            return true;
        }
        else if(messageType.equals(MessageType.STARTGAME) && args.length == 3)
        {
            return true;
        }
        else if(messageType.equals(MessageType.EXITGAMEACTIVITY) && args.length == 3)
        {
            return true;
        }
        else if(messageType.equals(MessageType.ROUNDSCORE) && args.length == 5)
        {
            this.playerId = args[3];
            this.roundAnswer = Integer.parseInt(args[4]);
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
        return appVersion.matches("\\d+");
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
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.name + " " + this.displayName + " " + this.playerId);
        }
        else if(messageType.equals(MessageType.INVITE))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.roomName + " " + this.displayName + " " + this.name + " " + this.playerId + " " + this.roomPort);
        }
        else if(messageType.equals(MessageType.REQUESTID))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString());
        }
        else if(messageType.equals(MessageType.ID))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.playerId + " " + this.displayName);
        }
        else if(messageType.equals(MessageType.ADDPLAYERLIST))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.name + " " + this.displayName + " " + this.playerId);
        }
        else if(messageType.equals(MessageType.REMOVEPLAYERLIST))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.playerId);
        }
        else if(messageType.equals(MessageType.ACTUALIZEROOMNAME))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.roomName);
        }
        else if(messageType.equals(MessageType.GAMEURLS))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + Integer.toString(numberOfUrls) + " " + printURLArrayList());
        }
        else if(messageType.equals(MessageType.READY))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.playerId);
        }
        else if(messageType.equals(MessageType.STARTGAME))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString());
        }
        else if(messageType.equals(MessageType.EXITGAMEACTIVITY))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString());
        }
        else if(messageType.equals(MessageType.ROUNDSCORE))
        {
            return (this.appName + " " + this.appVersion + " " + this.messageType.toString() + " " + this.playerId + " " + Integer.toString(this.roundAnswer));
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


    //Getters and setters
    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getMessageType() {return messageType.toString();}

    public String getName() {
        return this.name;
    }

    public String getRealName() {return Util.substituteUnder(this.name);}

    public String getDisplayName() {
        return this.displayName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getRoomName() {return this.roomName;}

    public String getRealRoomName() {return Util.substituteUnder(this.roomName);}

    public String getRoomPort() {return this.roomPort;}

    public String getRoundAnswer() {return Integer.toString(this.roundAnswer);}

    public String getTotalScore() {return Integer.toString(this.totalScore);}

    public ArrayList<String> getUrlsArrayList() {return this.urlsArrayList;}

    public String printURLArrayList()
    {
        if(this.urlsArrayList == null) return "";
        else
        {
            String str = "";

            for(String url : this.urlsArrayList)
            {
                str += url + " ";
            }

            str = str.substring(0, str.length() - 1);
            return str;
        }
    }

    @Override
    public String toString()
    {
        String str = new String(
                "MessageType: " + this.messageType.toString() +  "\n" +
                "Valid: " + this.valid + "\n" +
                "AppName: " + this.appName + "\n" +
                "AppVersion: " + this.appVersion + "\n" +
                "Name: " + this.name + "\n" +
                "DisplayName: " + this.displayName + "\n" +
                "ID" + this.playerId + "\n" +
                "RoomName: " + this.roomName + "\n" +
                "RoomPort: " + this.roomPort + "\n" +
                "NumberOfUrls: " + Integer.toString(numberOfUrls) + "\n" +
                "ArrayListURL:" + printURLArrayList() + "\n" +
                "RoundAnswer: " + getRoundAnswer() + "\n"  +
                "TotalScore: " + getTotalScore() + "\n");

        return str;
    }
}
