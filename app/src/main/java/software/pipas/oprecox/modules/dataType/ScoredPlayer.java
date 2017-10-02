package software.pipas.oprecox.modules.dataType;

public class ScoredPlayer
{
    private Player player;
    private int score;

    public ScoredPlayer(Player player, int score)
    {
        this.player = player;
        this.score = score;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Integer getScore()
    {
        return score;
    }
}
