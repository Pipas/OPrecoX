package software.pipas.oprecox.activities.singlePlayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.R;

public class GameOver extends AppCompatActivity
{

    private Intent intent;
    private int gameSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setTitle(R.string.gameover);

        intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        gameSize = intent.getIntExtra("gameSize", 0);
        int correctGuesses = intent.getIntExtra("correctGuesses", 0);

        TextView scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        TextView guesses = (TextView) findViewById(R.id.guesses);

        scoreOutput.setText(String.format("%d", score));
        guesses.setText(/*String.format("%d/%d", correctGuesses, gameSize)*/"");
    }

    public void pressFinish(View v)
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void pressRestart(View v) {}
}
