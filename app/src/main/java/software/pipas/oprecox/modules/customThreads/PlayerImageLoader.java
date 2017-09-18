package software.pipas.oprecox.modules.customThreads;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.Players;

import software.pipas.oprecox.modules.dataType.DataType;
import software.pipas.oprecox.modules.interfaces.OnPlayerImageLoader;

public class PlayerImageLoader extends AsyncTask<Void, Void, Void>
{
    private OnPlayerImageLoader onPlayerImageLoader;
    private GoogleApiClient mGoogleApiClient;
    private ArrayAdapter listAdapter;
    private DataType dataType;

    public PlayerImageLoader(OnPlayerImageLoader onPlayerImageLoader, GoogleApiClient mGoogleApiClient, ArrayAdapter listAdapter, DataType dataType)
    {
        this.onPlayerImageLoader = onPlayerImageLoader;
        this.mGoogleApiClient = mGoogleApiClient;
        this.listAdapter = listAdapter;
        this.dataType = dataType;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        PendingResult<Players.LoadPlayersResult> pendingResult = Games.Players.loadPlayer(this.mGoogleApiClient, dataType.getPlayerID());

        PlayerBuffer players = pendingResult.await().getPlayers();

        if(players.getCount() <= 0)
        {
            players.release();
            return null;
        }

        com.google.android.gms.games.Player googlePlayer = players.get(0);
        this.dataType.updateImage(googlePlayer.getIconImageUri());
        players.release();

        onPlayerImageLoader.onRefreshUI();

        return null;
    }
}
