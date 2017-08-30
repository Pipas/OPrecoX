package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;

public class PriceGuessGameActivity extends GameActivity
{
    private ArrayList<TextView> dialpadButtons = new ArrayList<>();
    private TextView priceGuess;
    private String dialpadString = "";
    private float guessPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        gameSize = intent.getIntExtra("gameSize", 10);

        inflateGuesserView();

        initiateCustomFonts();

        initiateButtonListeners();

        startDataParses();
    }

    private void inflateGuesserView()
    {
        FrameLayout guesserFrameLayout = (FrameLayout)findViewById(R.id.guesserFrameLayout);
        View child = getLayoutInflater().inflate(R.layout.price_guesser_layout, null);
        guesserFrameLayout.addView(child);
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

        priceGuess = (TextView)findViewById(R.id.priceGuess);
        TextView confirmButtonTextView = (TextView)findViewById(R.id.confirmButtonTextView);



        Typeface Antonio_Regular = Typeface.createFromAsset(getAssets(),  "font/Antonio-Regular.ttf");

        priceGuess.setTypeface(Antonio_Regular);
        confirmButtonTextView.setTypeface(Antonio_Regular);
        for(TextView dialpadButton : dialpadButtons)
        {
            dialpadButton.setTypeface(Antonio_Regular);
        }
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

        ImageView dialpadBackspace = (ImageView) findViewById(R.id.dialpadBackspace);
        dialpadBackspace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateNumber(-1);
            }
        });

        LinearLayout confirmButton = (LinearLayout) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                confirmSelection();
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

        priceGuess.setText(outputFormat);

        Log.d("INPUT", String.format("%s | %s", dialpadString, priceGuess.getText()));
    }

    private void confirmSelection()
    {
        Intent myIntent = new Intent(this, AfterGuessActivity.class);
        myIntent.putExtra("score", score);
        myIntent.putExtra("correctPrice", app.getAd(adIndex).getPrice());
        myIntent.putExtra("guessPrice", guessPrice);
        myIntent.putExtra("adIndex", adIndex);
        startActivityForResult(myIntent, 2 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                int page = data.getIntExtra("page", 0);
                imagePreview.setCurrentItem(page);
            }
        }
        if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                score = data.getIntExtra("score", 0);
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
                priceGuess.setText("");
                togglePanel(null);
            }
        }
    }
}
