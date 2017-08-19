package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.CategoryChooser;
import software.pipas.oprecox.activities.other.GameSizeChooser;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;

public class Lobby extends AppCompatActivity implements ParsingCallingActivity
{
    private int gameSize = 10;
    private ProgressDialog progressDialog;
    private OPrecoX app;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_lobby);

        app = (OPrecoX) getApplicationContext();
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
        Typeface Comfortaa_Thin = Typeface.createFromAsset(getAssets(),  "font/Comfortaa_Regular.ttf");

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
                gameSize = data.getIntExtra("gameSize", 10);
                TextView numberGuessesTooltip = (TextView) findViewById(R.id.gameSizeTooltip);
                numberGuessesTooltip.setText(Integer.toString(gameSize));
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
        startProcessDialog();
        startDataParses();
    }

    private void startProcessDialog()
    {
        progressDialog = new ProgressDialog(Lobby.this, R.style.DialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processDialog));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void startDataParses()
    {
        app.setAds(new Ad[gameSize]);
        AsyncGetAll parsingAyncTask;
        for(int i = 0; i < 2; i++)
        {
            parsingAyncTask = new AsyncGetAll(this, app, i);
            parsingAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void selectCategory(View v)
    {
        Intent myIntent = new Intent(this, CategoryChooser.class);
        startActivity(myIntent);
    }

    public void selectGameSize(View v)
    {
        Intent myIntent = new Intent(this, GameSizeChooser.class);
        startActivityForResult(myIntent, 2);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void parsingEnded()
    {
        if(app.getAd(0) != null && app.getAd(1) != null)
        {
            progressDialog.dismiss();
            Intent myIntent = new Intent(this, PriceGuessGameActivity.class);
            myIntent.putExtra("gameSize", gameSize);
            startActivity(myIntent);
        }
    }
}
