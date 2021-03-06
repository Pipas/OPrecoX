package software.pipas.oprecox.modules.customThreads;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.Players;

import software.pipas.oprecox.modules.dataType.DataType;
import software.pipas.oprecox.modules.dataType.Player;
import software.pipas.oprecox.modules.interfaces.OnPlayerImageLoader;
import software.pipas.oprecox.modules.interfaces.OnPlayerLoader;

public class PlayerLoader extends AsyncTask<Void, Void, Void>
{
    private OnPlayerLoader onPlayerLoader;
    private GoogleApiClient mGoogleApiClient;
    private DataType dataType;

    public PlayerLoader(OnPlayerLoader onPlayerLoader, GoogleApiClient mGoogleApiClient, DataType dataType)
    {
        this.onPlayerLoader = onPlayerLoader;
        this.mGoogleApiClient = mGoogleApiClient;
        this.dataType = dataType;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        Log.d("MY_IP_DEBUG","loading player");
        PendingResult<Players.LoadPlayersResult> pendingResult = Games.Players.loadPlayer(this.mGoogleApiClient, dataType.getPlayerID());
        Log.d("MY_IP_DEBUG","trying");
        PlayerBuffer players = pendingResult.await().getPlayers();
        Log.d("MY_IP_DEBUG","trying lllll");
        if(players.getCount() <= 0)
        {
            players.release();
            return null;
        }

        com.google.android.gms.games.Player googlePlayer = players.get(0);
        this.dataType.updateImage(googlePlayer.getIconImageUri());

        String playerName = googlePlayer.getName();
        if(playerName == null) playerName = googlePlayer.getDisplayName();

        Player player = new Player(
                playerName,
                googlePlayer.getDisplayName(),
                googlePlayer.getPlayerId(),
                googlePlayer.getIconImageUri(),
                -1,
                null,
                System.currentTimeMillis());


        players.release();
        onPlayerLoader.playerLoaded(player);
        Log.d("MY_IP_DEBUG","loaded sending callback");
        return null;
    }
}
