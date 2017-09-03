package software.pipas.oprecox.modules.customThreads;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.Players;

import software.pipas.oprecox.modules.dataType.DataType;
import software.pipas.oprecox.modules.dataType.Invite;
import software.pipas.oprecox.modules.interfaces.OnAsyncTaskCompleted;

/**
 * Created by nuno_ on 17-Aug-17.
 */

public class PlayerLoader extends AsyncTask<Void, Void, Void>
{
    private OnAsyncTaskCompleted asyncTaskCompleted;
    private GoogleApiClient mGoogleApiClient;
    private ArrayAdapter listAdapter;
    private DataType dataType;

    public PlayerLoader(OnAsyncTaskCompleted asyncTaskCompleted, GoogleApiClient mGoogleApiClient, ArrayAdapter listAdapter, DataType dataType)
    {
        this.asyncTaskCompleted = asyncTaskCompleted;
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

        asyncTaskCompleted.onRefreshUI();

        return null;
    }
}
