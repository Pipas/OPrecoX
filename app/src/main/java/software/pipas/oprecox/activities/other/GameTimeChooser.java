package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.util.Settings;

public class GameTimeChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_time_chooser);

        initiateCustomFonts();
    }

    private void initiateCustomFonts()
    {
        TextView gameTimeTitleTextView = (TextView)findViewById(R.id.gameTimeTitleTextView);
        TextView shortWaitTextView = (TextView)findViewById(R.id.shortWaitTextView);
        TextView normalWaitTextView = (TextView)findViewById(R.id.normalWaitTextView);
        TextView longWaitTextView = (TextView)findViewById(R.id.longWaitTextView);


        CustomFontHelper.setCustomFont(gameTimeTitleTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(shortWaitTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(normalWaitTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(longWaitTextView, "font/antipastopro-demibold.otf", getBaseContext());
    }

    public void pressshort(View v)
    {
        confirmSelection(30);
    }

    public void pressnormal(View v)
    {
        confirmSelection(60);
    }

    public void presslong(View v)
    {
        confirmSelection(120);
    }

    private void confirmSelection(int gameTime)
    {
        Intent returnIntent = new Intent();
        Settings.setGameTime(gameTime, this);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
