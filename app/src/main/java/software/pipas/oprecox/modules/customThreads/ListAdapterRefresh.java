package software.pipas.oprecox.modules.customThreads;

import android.view.View;
import android.widget.ArrayAdapter;

public class ListAdapterRefresh extends Thread
{
    private ArrayAdapter listAdapter;
    private View view1;
    private View view2;

    public ListAdapterRefresh(ArrayAdapter listAdapter, View view1, View view2)
    {
        this.listAdapter = listAdapter;
        this.view1 = view1;
        this.view2 = view2;
    }

    @Override
    public void run()
    {
        listAdapter.notifyDataSetChanged();
        if(view1 != null)
        {
            if(listAdapter.getCount() == 0)
                view1.setVisibility(View.VISIBLE);
            else
                view1.setVisibility(View.GONE);
        }

        if(view2 != null)
        {
            if(listAdapter.getCount() == 0)
                view2.setVisibility(View.VISIBLE);
            else
                view2.setVisibility(View.GONE);
        }
    }


}
