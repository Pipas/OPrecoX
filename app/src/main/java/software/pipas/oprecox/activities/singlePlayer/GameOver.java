package software.pipas.oprecox.activities.singlePlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.adapters.AdOverviewAdapter;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.modules.database.DatabaseHandler;

public class GameOver extends AppCompatActivity
{
    private AdOverviewAdapter adOverviewAdapter;
    private OPrecoX app;
    private int score;
    private ListView adOverviewList;
    private TextView scoreOutput;
    private DatabaseHandler database;
    private ArrayList<Integer> savedAds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        app = (OPrecoX) getApplicationContext();
        if(app.getAds() == null)
        {
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        database = new DatabaseHandler(this);

        scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        TextView finalScoreTextView = (TextView) findViewById(R.id.finalScoreTextView);
        TextView gameOverTitle = (TextView) findViewById(R.id.gameOverTitle);

        adOverviewList = (ListView) findViewById(R.id.adOverviewList);

        CustomFontHelper.setCustomFont(scoreOutput, "font/Comfortaa_Thin.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(finalScoreTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameOverTitle, "font/antipastopro-demibold.otf", getBaseContext());

        initiateViews();
    }

    private void initiateViews()
    {
        ArrayList<Ad> ads = new ArrayList<>(Arrays.asList(app.getAds()));
        adOverviewAdapter = new AdOverviewAdapter(ads, getApplicationContext(), getContentResolver());
        adOverviewList.setAdapter(adOverviewAdapter);
        adOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                saveAd(position);
            }
        });

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pressFinish();
            }
        });

        scoreOutput.setText(String.format("%d", score));
    }

    private void saveAd(int index)
    {
        if(!savedAds.contains(index))
        {
            database.open();
            ArrayList<AdPreview> previews = database.getAllAds();
            for(AdPreview preview : previews)
            {
                if(preview.getTitle().equals(app.getAd(index).getTitle()))
                {
                    database.close();
                    savedAds.add(index);
                    return;
                }
            }
            database.createAd(app.getAd(index));
            database.close();
            Toast.makeText(getBaseContext(), getResources().getString(R.string.savedAd), Toast.LENGTH_SHORT).show();
            savedAds.add(index);
        }
    }

    private void pressFinish()
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
