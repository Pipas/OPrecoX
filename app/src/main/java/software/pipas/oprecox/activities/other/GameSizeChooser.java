package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.util.Settings;

public class GameSizeChooser extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        Boolean multiplayer = intent.getBooleanExtra("multiplayer", false);
        if(multiplayer)
            setTheme(R.style.MultiplayerTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamesize_chooser);

        if(multiplayer)
        {
            LinearLayout headerLayout = (LinearLayout) findViewById(R.id.headerLayout);
            headerLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.purple));
        }

        initiateCustomFonts();
    }

    private void initiateCustomFonts()
    {
        TextView gameSizeTitleTextView = (TextView)findViewById(R.id.gameSizeTitleTextView);
        TextView shortGameTextView = (TextView)findViewById(R.id.shortGameTextView);
        TextView normalGameTextView = (TextView)findViewById(R.id.normalGameTextView);
        TextView longGameTextView = (TextView)findViewById(R.id.longGameTextView);

        TextView shortGameTooltip = (TextView)findViewById(R.id.shortGameTooltip);
        TextView normalGameTooltip = (TextView)findViewById(R.id.normalGameTooltip);
        TextView longGameTooltip = (TextView)findViewById(R.id.longGameTooltip);

        CustomFontHelper.setCustomFont(gameSizeTitleTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(shortGameTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(normalGameTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(longGameTextView, "font/antipastopro-demibold.otf", getBaseContext());

        CustomFontHelper.setCustomFont(shortGameTooltip, "font/Comfortaa_Thin.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(normalGameTooltip, "font/Comfortaa_Thin.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(longGameTooltip, "font/Comfortaa_Thin.ttf", getBaseContext());
    }

    public void pressguess5(View v)
    {
        confirmSelection(5);
    }

    public void pressguess10(View v)
    {
        confirmSelection(10);
    }

    public void pressguess20(View v)
    {
        confirmSelection(20);
    }

    private void confirmSelection(int gameSize)
    {
        Intent returnIntent = new Intent();
        Settings.setGameSize(gameSize, this);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
