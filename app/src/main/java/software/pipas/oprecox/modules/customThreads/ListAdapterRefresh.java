package software.pipas.oprecox.modules.customThreads;

import software.pipas.oprecox.modules.adapters.PlayerListAdapter;

/**
 * Created by nuno_ on 15-Aug-17.
 */

public class ListAdapterRefresh extends Thread
{
    private PlayerListAdapter playerListAdapter;

    public ListAdapterRefresh(PlayerListAdapter playerListAdapter)
    {
        this.playerListAdapter = playerListAdapter;
    }

    @Override
    public void run()
    {
        playerListAdapter.notifyDataSetChanged();
    }


}
