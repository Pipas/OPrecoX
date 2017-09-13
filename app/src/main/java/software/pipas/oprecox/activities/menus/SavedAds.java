package software.pipas.oprecox.activities.menus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.AdListAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.modules.database.DatabaseHandler;

public class SavedAds extends AppCompatActivity
{
    private DatabaseHandler database;
    private AdListAdapter adListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_adds);

        TextView savedAdsTitleTextView = (TextView)findViewById(R.id.savedAdsTitleTextView);
        TextView savedAdsTooltip = (TextView)findViewById(R.id.savedAdsTooltip);

        CustomFontHelper.setCustomFont(savedAdsTitleTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(savedAdsTooltip, "font/Conforta_Regular.ttf", getBaseContext());

        database = new DatabaseHandler(this);
        database.open();
        DynamicListView myAddsList = (DynamicListView) findViewById(R.id.myAddsList);

        ArrayList<AdPreview> ads = database.getAllComments();

        if(!ads.isEmpty())
        {
            savedAdsTooltip.setVisibility(View.GONE);
        }

        adListAdapter = new AdListAdapter(ads, getApplicationContext(), getContentResolver());
        SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(adListAdapter);
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
        myAddsList.setAdapter(adListAdapter);
        myAddsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adListAdapter.getItem(position).getUrl()));
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
        database.deleteComment(adListAdapter.getItem(position));
        adListAdapter.remove(position);
    }


}
