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
    ROUNDSCORE("ROUNDSCORE"),               //clients to host, round answered with score
    CONTINUEGAME("CONTINUEGAME"),           //from wait to show answer (should give his round details (answer and score)
    NEXTROUND("NEXTROUND"),                 //from showing to next round
    GAMEOVER("GAMEOVER"),                   //from wait to gameover screen (should give list with everyone final scores)
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
