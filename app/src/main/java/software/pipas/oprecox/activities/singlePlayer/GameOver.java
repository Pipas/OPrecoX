package software.pipas.oprecox.activities.singlePlayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.activities.menus.MainMenu;
import software.pipas.oprecox.R;

public class GameOver extends AppCompatActivity
{

    private Intent intent;
    private int NGUESSES;

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
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("NGUESSES", NGUESSES);
        startActivity(myIntent);
    }
}
