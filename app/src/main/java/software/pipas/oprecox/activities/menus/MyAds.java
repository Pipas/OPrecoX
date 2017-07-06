package software.pipas.oprecox.activities.menus;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.AddListAdapter;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.modules.database.DatabaseHandler;
import software.pipas.oprecox.util.Settings;

public class MyAds extends AppCompatActivity
{
    private DatabaseHandler database;
    private AddListAdapter addListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adds);

        setTitle("Anúncios Guardados");

        Settings.resetNewSavedAds(getSharedPreferences("gameSettings", MODE_PRIVATE).edit());

        database = new DatabaseHandler(this);
        database.open();
        DynamicListView myAddsList = (DynamicListView) findViewById(R.id.myAddsList);

        ArrayList<AdPreview> ads = database.getAllComments();

        addListAdapter = new AddListAdapter(ads, getApplicationContext(), getContentResolver());
        SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(addListAdapter);
        animationAdapter.setAbsListView(myAddsList);
        myAddsList.setAdapter(animationAdapter);
        myAddsList.enableSwipeToDismiss(
                new OnDismissCallback()
                {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions)
                        {
                            removeAdFromDatabase(position);
                        }
                    }
                }
        );
        myAddsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(addListAdapter.getItem(position).getUrl()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        database.close();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        database.open();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


    private void removeAdFromDatabase(int position)
    {
        database.deleteComment(addListAdapter.getItem(position));
        addListAdapter.remove(position);
    }


}
