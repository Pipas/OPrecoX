package software.pipas.oprecox.modules.customThreads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import software.pipas.oprecox.modules.adapters.PlayerListAdapter;
import software.pipas.oprecox.modules.dataType.Player;

import software.pipas.oprecox.R;
/**
 * Created by nuno_ on 16-Aug-17.
 */

public class PlayerListUpdater extends Thread
{
    private Activity activity;
    private ArrayList<Player> players;
    private PlayerListAdapter playerListAdapter;
    private boolean closed;

    private int timeBetweenRefresh;
    private int timeOfPlayerExpired;


    public PlayerListUpdater(Activity activity, ArrayList<Player> players, PlayerListAdapter playerListAdapter)
    {
        this.activity = activity;
        this.players = players;
        this.playerListAdapter = playerListAdapter;
        this.closed = false;
        this.timeBetweenRefresh = this.activity.getResources().getInteger(R.integer.TIME_BETWEEN_UPDATES);
        this.timeOfPlayerExpired = this.activity.getResources().getInteger(R.integer.TIME_LIMIT_FOR_PLAYER_LIST_REFRESH);

    }

    @Override
    public void run()
    {
        while(!this.closed)
        {
            if(this.update()) {this.refresh();}
            this.sleep();
        }
    }


    public void close()
    {
        this.closed = true;
    }


    private boolean update()
    {
        boolean update = false;
        ArrayList<Integer> indexes = new ArrayList<>();

        for (Player player : this.players)
        {
            int index = this.players.indexOf(player);
            long timeAnnounced = player.getTimeAnnounced();
            long diff = System.currentTimeMillis() - timeAnnounced;

            if (diff >= this.timeOfPlayerExpired)
            {
                indexes.add(index);
                update = true;
            }
        }


        if(update)
        {
            for(int index : indexes)
            {
                this.players.remove(index);
            }
        }

        return update;
    }


    private void refresh()
    {
        ListAdapterRefresh listAdapterRefresh = new ListAdapterRefresh(this.playerListAdapter);
        this.activity.runOnUiThread(listAdapterRefresh);
    }

    private void sleep()
    {
        try {Thread.sleep(this.timeBetweenRefresh);}
        catch (InterruptedException e) {e.printStackTrace();}
    }



}
