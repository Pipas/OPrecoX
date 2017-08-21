package software.pipas.oprecox.activities.singlePlayer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import software.pipas.oprecox.R;

public class PriceGuessGameActivity extends RevampedGameActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        gameSize = intent.getIntExtra("gameSize", 10);

        inflateGuesserView();

        initiateCustomFonts();

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
        TextView priceGuess = (TextView)findViewById(R.id.priceGuess);
        TextView confirmButtonTextView = (TextView)findViewById(R.id.confirmButtonTextView);
        TextView dialpad1 = (TextView)findViewById(R.id.dialpad1);
        TextView dialpad2 = (TextView)findViewById(R.id.dialpad2);
        TextView dialpad3 = (TextView)findViewById(R.id.dialpad3);
        TextView dialpad4 = (TextView)findViewById(R.id.dialpad4);
        TextView dialpad5 = (TextView)findViewById(R.id.dialpad5);
        TextView dialpad6 = (TextView)findViewById(R.id.dialpad6);
        TextView dialpad7 = (TextView)findViewById(R.id.dialpad7);
        TextView dialpad8 = (TextView)findViewById(R.id.dialpad8);
        TextView dialpad9 = (TextView)findViewById(R.id.dialpad9);
        TextView dialpad0 = (TextView)findViewById(R.id.dialpad0);
        TextView dialpadDot = (TextView)findViewById(R.id.dialpadDot);


        Typeface Antonio_Regular = Typeface.createFromAsset(getAssets(),  "font/Antonio-Regular.ttf");

        priceGuess.setTypeface(Antonio_Regular);
        confirmButtonTextView.setTypeface(Antonio_Regular);
        dialpad1.setTypeface(Antonio_Regular);
        dialpad2.setTypeface(Antonio_Regular);
        dialpad3.setTypeface(Antonio_Regular);
        dialpad4.setTypeface(Antonio_Regular);
        dialpad5.setTypeface(Antonio_Regular);
        dialpad6.setTypeface(Antonio_Regular);
        dialpad7.setTypeface(Antonio_Regular);
        dialpad8.setTypeface(Antonio_Regular);
        dialpad9.setTypeface(Antonio_Regular);
        dialpad0.setTypeface(Antonio_Regular);
        dialpadDot.setTypeface(Antonio_Regular);
    }
}
