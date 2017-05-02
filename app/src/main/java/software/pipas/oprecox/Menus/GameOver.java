package software.pipas.oprecox.Menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import software.pipas.oprecox.GameActivity;
import software.pipas.oprecox.R;

import java.util.ArrayList;

import static software.pipas.oprecox.R.id.scoreOutput;

public class GameOver extends AppCompatActivity
{

    private Intent intent;
    private int NGUESSES;
    private boolean gameType;
    private String roomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setTitle(R.string.gameover);

        intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        NGUESSES = intent.getIntExtra("NGUESSES", 0);
        int correctGuesses = intent.getIntExtra("correctGuesses", 0);
        gameType = intent.getBooleanExtra("gameType", false);

        if(gameType)
        {
            RelativeLayout restartButton = (RelativeLayout) findViewById(R.id.restartButton);
            restartButton.setVisibility(View.GONE);
            View fillerView = (View) findViewById(R.id.fillerView);
            fillerView.setVisibility(View.VISIBLE);
            roomCode = intent.getStringExtra("roomCode");
        }

        TextView scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        TextView guesses = (TextView) findViewById(R.id.guesses);

        scoreOutput.setText(String.format("%d", score));
        guesses.setText(String.format("%d/%d", correctGuesses, NGUESSES));
    }

    public void pressFinish(View v)
    {
        Intent myIntent = new Intent(this, MainMenu.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent myIntent = new Intent(this, MainMenu.class);
        startActivity(myIntent);
        finish();
    }

    public void pressRestart(View v)
    {
        ArrayList<String> selected = intent.getStringArrayListExtra("categories");
        ArrayList<String> urls = new ArrayList<>();
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("urls", urls);
        myIntent.putExtra("NGUESSES", NGUESSES);
        myIntent.putExtra("categories", selected);
        startActivity(myIntent);
    }
}
