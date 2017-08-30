package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
    private int scorePlus;
    private float correctPrice;
    private float guessPrice;
    private int adIndex;

    private DatabaseHandler database;
    private Boolean adSaved = false;

    private TextView priceGuess;
    private TextView correctPriceOutput;
    private TextView scoreOutput;
    private TextView scorePlusOutput;

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

        initiateViews();

        populateViews();
    }

    private void initiateViews()
    {
        priceGuess = (TextView) findViewById(R.id.priceGuess);
        correctPriceOutput = (TextView) findViewById(R.id.correctPriceOutput);
        scoreOutput = (TextView) findViewById(R.id.scoreOutput);
        scorePlusOutput = (TextView) findViewById(R.id.scorePlus);
    }

    private void populateViews()
    {
        if(guessPrice == (int) guessPrice)
            priceGuess.setText(String.format("%,d€", (int) guessPrice));
        else
            priceGuess.setText(String.format("%,.2f€", guessPrice));


        if(correctPrice == (int) correctPrice)
            correctPriceOutput.setText(String.format("%,d€", (int) correctPrice));
        else
            correctPriceOutput.setText(String.format("%,.2f€", correctPrice));

        scoreOutput.setText(String.format("%d", score));

        if (calculateScore())
        {
            scorePlusOutput.setText(String.format("+%d", scorePlus));
            scorePlusOutput.setVisibility(View.VISIBLE);
            priceGuess.setTextColor(Color.parseColor("#4caf50"));
        }
        else
            priceGuess.setTextColor(Color.parseColor("#f44336"));
    }

    private Boolean calculateScore()
    {
        if(correctPrice == guessPrice)
            scorePlus = 50;
        else if(correctPrice <= 30)
        {
            if(guessPrice >= 0.75*correctPrice && guessPrice <= 1.25*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.5*correctPrice && guessPrice <= 1.5*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
                return false;
            }
        }
        else if(correctPrice <= 10000)
        {
            if(guessPrice >= 0.90*correctPrice && guessPrice <= 1.10*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.75*correctPrice && guessPrice <= 1.25*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
                return false;
            }
        }
        else
        {
            if(guessPrice >= 0.90*correctPrice && guessPrice <= 1.10*correctPrice)
                scorePlus = 25;
            else if(guessPrice >= 0.85*correctPrice && guessPrice <= 1.15*correctPrice)
                scorePlus = 10;
            else
            {
                scorePlus = 0;
                return false;
            }
        }

        return true;
    }

    public void pressContinue(View v)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("score", score+scorePlus);
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

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
