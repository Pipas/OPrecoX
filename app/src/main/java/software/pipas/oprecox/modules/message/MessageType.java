package software.pipas.oprecox.modules.message;

public enum MessageType
{
    ANNOUNCE("ANNOUNCE"),
    INVITE("INVITE"),
    REQUESTID("REQUESTID"),
    ID("ID"),
    ADDPLAYERLIST("ADDPLAYERLIST"),
    REMOVEPLAYERLIST("REMOVEPLAYERLIST"),
    ACTUALIZEROOMNAME("ACTUALIZEROOMNAME"),
    GAMEURLS("GAMEURLS"),
    READY("READY"),
    STARTGAME("STARTGAME"),
    EXITGAMEACTIVITY("EXITGAMEACTIVITY"),
    ROUNDSCORE("ROUNDSCORE"),
    NEXTROUND("NEXTROUND");

    private final String message;

    private MessageType(String operation)
    {
        this.message = operation;
    }

    @Override
    public String toString()
    {
        return message;
    }

}
