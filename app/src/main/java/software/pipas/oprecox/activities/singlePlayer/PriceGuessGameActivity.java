package software.pipas.oprecox.activities.singlePlayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.database.DatabaseHandler;

import static software.pipas.oprecox.R.id.priceGuess;

public class PriceGuessGameActivity extends GameActivity
{
    private View afterGuess;
    private View priceGuesser;

    private ArrayList<TextView> dialpadButtons = new ArrayList<>();
    private TextView beforePriceGuess;
    private TextView afterPriceGuess;
    private TextView scorePlusTextView;
    private TextView correctPriceOutput;
    private TextView scoreOutput;

    private String dialpadString = "";
    private float guessPrice;

    private int scorePlus;

    private DatabaseHandler database;
    private Boolean adSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        gameSize = intent.getIntExtra("gameSize", 10);

        database = new DatabaseHandler(this);

        inflateGuesserViews();

        initiateCustomFonts();

        initiateButtonListeners();

        startDataParses();
    }

    private void inflateGuesserViews()
    {
        FrameLayout guesserFrameLayout = (FrameLayout)findViewById(R.id.guesserFrameLayout);
        priceGuesser = getLayoutInflater().inflate(R.layout.price_guesser_layout, null);
        guesserFrameLayout.addView(priceGuesser);

        afterGuess = getLayoutInflater().inflate(R.layout.after_guess_layout, null);
        guesserFrameLayout.addView(afterGuess);
        afterGuess.setVisibility(View.GONE);
    }

    private void initiateCustomFonts()
    {
        for(int i = 0; i < 10; i++)
        {
            String buttonID = "dialpad" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            TextView diapladButton = ((TextView) findViewById(resID));
            dialpadButtons.add(diapladButton);
        }

        TextView dialpadDot = (TextView)findViewById(R.id.dialpadDot);
        dialpadButtons.add(dialpadDot);

        beforePriceGuess = (TextView) priceGuesser.findViewById(priceGuess);
        TextView confirmButtonTextView = (TextView)findViewById(R.id.confirmButtonTextView);

        CustomFontHelper.setCustomFont(beforePriceGuess, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(confirmButtonTextView, "font/Antonio-Regular.ttf", getBaseContext());

        for(TextView dialpadButton : dialpadButtons)
        {
            CustomFontHelper.setCustomFont(dialpadButton, "font/Antonio-Regular.ttf", getBaseContext());
        }

        afterPriceGuess = (TextView) afterGuess.findViewById(priceGuess);
        TextView correctPriceOutputTextView = (TextView) afterGuess.findViewById(R.id.correctPriceOutputTextView);
        correctPriceOutput = (TextView) afterGuess.findViewById(R.id.correctPriceOutput);

        TextView scoreOutputTextView = (TextView) afterGuess.findViewById(R.id.scoreOutputTextView);
        scoreOutput = (TextView) afterGuess.findViewById(R.id.scoreOutput);
        scorePlusTextView = (TextView) afterGuess.findViewById(R.id.scorePlus);
        TextView continueButtonTextView = (TextView) findViewById(R.id.continueButtonTextView);

        CustomFontHelper.setCustomFont(afterPriceGuess, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(correctPriceOutputTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(correctPriceOutput, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(scoreOutputTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(scoreOutput, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(scorePlusTextView, "font/Antonio-Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(continueButtonTextView, "font/Antonio-Regular.ttf", getBaseContext());
    }

    private void initiateButtonListeners()
    {
        for(int i = 0; i < 11; i++)
        {
            final int tempIndex = i;
            dialpadButtons.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    updateNumber(tempIndex);
                }
            });
        }

        ImageView dialpadBackspace = (ImageView) priceGuesser.findViewById(R.id.dialpadBackspace);
        dialpadBackspace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateNumber(-1);
            }
        });

        ImageView savedAdsButton = (ImageView) afterGuess.findViewById(R.id.savedAdsButton);
        savedAdsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveAd();
            }
        });

        LinearLayout confirmButton = (LinearLayout) priceGuesser.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                confirmSelection();
            }
        });

        LinearLayout continueButton = (LinearLayout) afterGuess.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                continuePressed();
            }
        });
    }

    private void updateNumber(int digit)
    {
        String outputFormat;

        if(digit == -1)
        {
            if (dialpadString != null && dialpadString.length() > 0)
            {
                dialpadString = dialpadString.substring(0, dialpadString.length()-1);
            }
            else
                return;
        }
        else if(dialpadString.length() >= 9 || (dialpadString.contains(".") && dialpadString.substring(dialpadString.lastIndexOf(".") + 1).length() >= 2))
            return;


        if(digit == 0)
        {
            if (dialpadString.length() == 0)
                    return;

            dialpadString += digit;
        }
        if(digit > 0 && digit < 10)
        {
            dialpadString += digit;
        }
        else if(digit == 10)
        {
            if(dialpadString.contains("."))
                return;
            else if(dialpadString.length() >= 5 || dialpadString.length() == 0)
                return;
            else
                dialpadString += ".";
        }

        if (dialpadString != null && dialpadString.length() > 0)
        {
            guessPrice = Float.parseFloat(dialpadString);
            if(guessPrice == (int) guessPrice)
                outputFormat = String.format("%,d", (int) guessPrice);
            else
                outputFormat = String.format("%,.2f", guessPrice);

            if(dialpadString.substring(dialpadString.length() - 1).equals("."))
            {
                outputFormat += ".";
            }

            outputFormat += "€";
        }
        else
            outputFormat = "";

        beforePriceGuess.setText(outputFormat);

        Log.d("INPUT", String.format("%s | %s", dialpadString, beforePriceGuess.getText()));
    }

    private Boolean calculateScore()
    {
        float correctPrice = app.getAd(adIndex).getPrice();

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

    private void confirmSelection()
    {
        priceGuesser.setVisibility(View.GONE);
        afterGuess.setVisibility(View.VISIBLE);

        populateAfterGuessViews();
    }

    private void continuePressed()
    {
        if(adIndex >= gameSize - 1)
        {
            Intent myIntent = new Intent(this, GameOver.class);
            myIntent.putExtra("gameSize", gameSize);
            myIntent.putExtra("score", score);
            startActivity(myIntent);
            finish();
        }
        else
            adIndex++;

        setViewsWithAd(app.getAd(adIndex));
        dialpadString = "";
        beforePriceGuess.setText("");
        scorePlusTextView.setVisibility(View.GONE);
        togglePanel(null);

        priceGuesser.setVisibility(View.VISIBLE);
        afterGuess.setVisibility(View.GONE);
    }

    private void saveAd()
    {
        if(!adSaved)
        {
            database.open();
            database.createAd(app.getAd(adIndex));
            database.close();
            adSaved = true;
        }
        Toast.makeText(getBaseContext(), getResources().getString(R.string.savedAd), Toast.LENGTH_SHORT).show();
    }

    private void populateAfterGuessViews()
    {
        float correctPrice = app.getAd(adIndex).getPrice();

        if(guessPrice == (int) guessPrice)
            afterPriceGuess.setText(String.format("%,d€", (int) guessPrice));
        else
            afterPriceGuess.setText(String.format("%,.2f€", guessPrice));


        if(correctPrice == (int) correctPrice)
            correctPriceOutput.setText(String.format("%,d€", (int) correctPrice));
        else
            correctPriceOutput.setText(String.format("%,.2f€", correctPrice));

        scoreOutput.setText(String.format("%d", score));

        if (calculateScore())
        {
            scorePlusTextView.setText(String.format("+%d", scorePlus));
            scorePlusTextView.setVisibility(View.VISIBLE);
            score += scorePlus;
        }
    }
}
