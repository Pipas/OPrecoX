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
    READY("READY"),                         //ready ack
    STARTGAME("STARTGAME"),                 //command to start game
    EXITGAMEACTIVITY("EXITGAMEACTIVITY"),   //command to exit the game activity
    ROUNDSCORE("ROUNDSCORE"),               //clients to host, round answered with score (gives, playerid, round, answer and score)
    CONTINUEGAME("CONTINUEGAME"),           //from wait to after (should give all round details (player, answer and score))
    NEXTROUND("NEXTROUND"),                 //from after to before
    GAMEOVER("GAMEOVER"),                   //from after to gameover screen (should give list with everyone final scores)
    GAMEOVERTOLOBBY("GAMEOVERTOLOBBY");     //from gameover to lobby


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
