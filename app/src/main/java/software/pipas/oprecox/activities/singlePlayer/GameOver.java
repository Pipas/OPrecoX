package software.pipas.oprecox.activities.singlePlayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.AdOverviewAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;

public class GameOver extends AppCompatActivity
{
    private AdOverviewAdapter adOverviewAdapter;
    private OPrecoX app;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        TextView scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        TextView finalScoreTextView = (TextView) findViewById(R.id.finalScoreTextView);
        TextView gameOverTitle = (TextView) findViewById(R.id.gameOverTitle);

        ListView adOverviewList = (ListView) findViewById(R.id.adOverviewList);

        CustomFontHelper.setCustomFont(scoreOutput, "font/Comfortaa_Thin.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(finalScoreTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameOverTitle, "font/antipastopro-demibold.otf", getBaseContext());

        scoreOutput.setText(String.format("%d", score));

        app = (OPrecoX) getApplicationContext();

        ArrayList<Ad> ads = new ArrayList<>(Arrays.asList(app.getAds()));
        Log.d("DEBUG", Integer.toString(ads.size()));

        adOverviewAdapter = new AdOverviewAdapter(ads, getApplicationContext(), getContentResolver());
        adOverviewList.setAdapter(adOverviewAdapter);
        adOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adOverviewAdapter.getItem(position).getUrl()));
                startActivity(browserIntent);
            }
        });
    }

    public void pressFinish(View v)
    {
        resetAdArray();
        finish();
    }

    @Override
    public void onBackPressed()
    {
        resetAdArray();
        finish();
    }

    public void resetAdArray()
    {
        app.setAds(null);
    }
}
