package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.R;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.database.DatabaseHandler;

public class AfterGuessActivity extends AppCompatActivity
{
    private int score;
    private float correctPrice;
    private float guessPrice;
    private int adIndex;

    private DatabaseHandler database;
    private Boolean adSaved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_guess);

        database = new DatabaseHandler(this);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        correctPrice = intent.getFloatExtra("correctPrice", 0);
        guessPrice = intent.getFloatExtra("guessPrice", 0);
        adIndex = intent.getIntExtra("adIndex", 0);

        TextView priceGuess = (TextView) findViewById(R.id.priceGuess);
        TextView correctPriceOutput = (TextView) findViewById(R.id.correctPriceOutput);
        TextView scoreOutput = (TextView) findViewById(R.id.scoreOutput);

        if(guessPrice == (int) guessPrice)
            priceGuess.setText(String.format("%d", (int) guessPrice));
        else
            priceGuess.setText(String.format("%.2f", guessPrice));


        if(correctPrice == (int) correctPrice)
            correctPriceOutput.setText(String.format("%d", (int) correctPrice));
        else
            correctPriceOutput.setText(String.format("%.2f", correctPrice));

        scoreOutput.setText(String.format("%d", score));
    }

    public void pressContinue(View v)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("score", score);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void saveAd(View v)
    {
        OPrecoX app = (OPrecoX) getApplicationContext();

        if(!adSaved)
        {
            database.open();
            database.createAd(app.getAd(adIndex));
            database.close();
            adSaved = true;
        }
        Toast.makeText(this, "Anúncio Guardado", Toast.LENGTH_SHORT).show();
    }
}
