package software.pipas.oprecox.Menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.GameActivity;
import software.pipas.oprecox.R;

import java.util.ArrayList;

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
        Intent myIntent = new Intent(this, SinglePlayerOptions.class);
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
