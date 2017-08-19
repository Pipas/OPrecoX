package software.pipas.oprecox.modules.customThreads;

import android.widget.ArrayAdapter;

/**
 * Created by nuno_ on 15-Aug-17.
 */

public class ListAdapterRefresh extends Thread
{
    private ArrayAdapter listAdapter;

    public ListAdapterRefresh(ArrayAdapter listAdapter)
    {
        this.listAdapter = listAdapter;
    }

    @Override
    public void run()
    {
        listAdapter.notifyDataSetChanged();
    }


}
