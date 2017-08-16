package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.CategoryChooser;
import software.pipas.oprecox.activities.other.NGuessesChooser;

public class Lobby extends AppCompatActivity
{
    private ArrayList<String> urls = new ArrayList<String>();
    private int NGUESSES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_lobby);

        initiateCustomFonts();
    }

    private void initiateCustomFonts()
    {
        TextView singleplayerTitleTextView = (TextView)findViewById(R.id.singleplayerTitleTextView);
        TextView gameTypeButtonTextView = (TextView)findViewById(R.id.gameTypeButtonTextView);
        TextView gameSizeButtonTextView = (TextView)findViewById(R.id.gameSizeButtonTextView);
        TextView categoriesButtonTextView = (TextView)findViewById(R.id.categoriesButtonTextView);

        TextView gameTypeTooltip = (TextView)findViewById(R.id.gameTypeTooltip);
        TextView gameSizeTooltip = (TextView)findViewById(R.id.gameSizeTooltip);

        Typeface Comfortaa_Bold = Typeface.createFromAsset(getAssets(),  "font/Comfortaa_Bold.ttf");
        Typeface Comfortaa_Thin = Typeface.createFromAsset(getAssets(),  "font/Comfortaa_Thin.ttf");

        singleplayerTitleTextView.setTypeface(Comfortaa_Bold);
        gameTypeButtonTextView.setTypeface(Comfortaa_Bold);
        gameSizeButtonTextView.setTypeface(Comfortaa_Bold);
        categoriesButtonTextView.setTypeface(Comfortaa_Bold);

        gameTypeTooltip.setTypeface(Comfortaa_Thin);
        gameSizeTooltip.setTypeface(Comfortaa_Thin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                NGUESSES = data.getIntExtra("NGUESSES", 10);
                TextView numberGuessesTooltip = (TextView) findViewById(R.id.gameSizeTooltip);
                numberGuessesTooltip.setText(Integer.toString(NGUESSES));
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void startGame(View v)
    {
        if(!isNetworkAvailable())
        {
            Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("urls", urls);
        myIntent.putExtra("NGUESSES", NGUESSES);
        startActivity(myIntent);
    }

    public void selectCategory(View v)
    {
        Intent myIntent = new Intent(this, CategoryChooser.class);
        startActivity(myIntent);
    }

    public void selectGuessNumber(View v)
    {
        Intent myIntent = new Intent(this, NGuessesChooser.class);
        startActivityForResult(myIntent, 2);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
