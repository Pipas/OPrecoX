package software.pipas.oprecox.modules.customThreads;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.Players;

import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.dataType.DataType;
import software.pipas.oprecox.modules.dataType.Player;

/**
 * Created by nuno_ on 17-Aug-17.
 */

public class PlayerLoader extends AsyncTask<Void, Void, Void>
{
    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private ArrayAdapter listAdapter;
    private DataType dataType;

    public PlayerLoader(Activity activity, GoogleApiClient mGoogleApiClient, ArrayAdapter listAdapter, DataType dataType)
    {
        this.activity = activity;
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

        this.refreshTheAdapter(this.activity, this.listAdapter);
        return null;
    }

    private void refreshTheAdapter(Activity activity, ArrayAdapter listAdapter)
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(listAdapter);
        activity.runOnUiThread(listAdapterRefresh);
    }



}
