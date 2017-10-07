package software.pipas.oprecox.activities.singlePlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.CategoryChooser;
import software.pipas.oprecox.activities.other.GameSizeChooser;
import software.pipas.oprecox.application.OPrecoX;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.modules.dataType.Ad;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;
import software.pipas.oprecox.modules.parsing.OlxParser;
import software.pipas.oprecox.util.Settings;
import software.pipas.oprecox.util.Util;

public class Options extends AppCompatActivity implements ParsingCallingActivity
{
    private int gameSize = 10;
    private ProgressDialog progressDialog;
    private OPrecoX app;
    private OlxParser olxParser;

    private Boolean blocked;
    private Boolean showedToast;
    private TextView gameSizeTooltip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_options);

        CategoryHandler.checkIfRestart(this);

        gameSizeTooltip = (TextView)findViewById(R.id.gameSizeTooltip);

        updateGameSize();

        app = (OPrecoX) getApplicationContext();
        initiateCustomFonts();

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               startGame();
            }
        });
    }

    private void initiateCustomFonts()
    {
        TextView singleplayerTitleTextView = (TextView)findViewById(R.id.singleplayerTitleTextView);
        TextView gameTypeButtonTextView = (TextView)findViewById(R.id.gameTypeButtonTextView);
        TextView gameSizeButtonTextView = (TextView)findViewById(R.id.gameSizeButtonTextView);
        TextView categoriesButtonTextView = (TextView)findViewById(R.id.categoriesButtonTextView);

        TextView gameTypeTooltip = (TextView)findViewById(R.id.gameTypeTooltip);

        CustomFontHelper.setCustomFont(singleplayerTitleTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameTypeButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameSizeButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(categoriesButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameTypeTooltip, "font/Comfortaa_Regular.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(gameSizeTooltip, "font/Comfortaa_Thin.ttf", getBaseContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                updateGameSize();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void startGame()
    {
        if(!Util.isNetworkAvailable(getApplicationContext()))
        {
            Toasty.error(this, "Acesso à internet indisponivel.", Toast.LENGTH_SHORT, true).show();
            return;
        }
        startProcessDialog();
        startDataParses();
    }

    private void startProcessDialog()
    {
        AlertDialog.Builder popup;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            progressDialog = new ProgressDialog(Options.this, R.style.DialogTheme);
        else
            progressDialog = new ProgressDialog(Options.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processDialog));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void startDataParses()
    {
        olxParser = new OlxParser();
        app.setAds(new Ad[gameSize]);
        AsyncGetAll parsingAyncTask;
        for(int i = 0; i < 2; i++)
        {
            parsingAyncTask = new AsyncGetAll(this, app, i, olxParser);
            parsingAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void updateGameSize()
    {
        gameSize = Settings.getGameSize();
        gameSizeTooltip.setText(Integer.toString(gameSize));
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

    public void pressGameType(View v)
    {
        if(!showedToast)
        {
            Toasty.info(this, getString(R.string.comingsoon), Toast.LENGTH_SHORT, true).show();
            showedToast = true;
        }
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

    @Override
    public void olxChangeException()
    {
        if(!blocked)
        {
            blocked = true;
            Toasty.error(this, getString(R.string.olxError), Toast.LENGTH_LONG, true).show();
        }
    }
}
