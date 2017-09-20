package software.pipas.oprecox.activities.menus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.Hub;
import software.pipas.oprecox.activities.singlePlayer.Options;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.util.Settings;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;
    private Boolean showToast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Settings.setDeviceDisplayMetrics(getResources().getDisplayMetrics());

        initiateCustomFonts();

        CategoryHandler.checkIfRestart(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder popup;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            popup = new AlertDialog.Builder(this, R.style.DialogTheme);
        else
            popup = new AlertDialog.Builder(this);

        popup.setTitle(getString(R.string.leaveapp));
        popup.setMessage(getString(R.string.leaveappsub));
        popup.setCancelable(true);

        popup.setPositiveButton
                (
                R.string.leave,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                });

        popup.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = popup.create();
        alert.show();
    }

    private void initiateCustomFonts()
    {
        TextView singleplayerButtonTextView = (TextView)findViewById(R.id.singleplayerButtonTextView);
        TextView multiplayerButtonTextView = (TextView)findViewById(R.id.multiplayerButtonTextView);
        TextView savedAdsButtonTextView = (TextView)findViewById(R.id.savedAdsButtonTextView);

        CustomFontHelper.setCustomFont(singleplayerButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(multiplayerButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(savedAdsButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
    }

    public void pressSinglePlayer(View v)
    {
        Intent myIntent = new Intent(this, Options.class);
        startActivity(myIntent);
    }

    public void pressMultiPlayer(View v)
    {
        /*count++;

        if(count >= 10)
        {
            if(!Util.isNetworkAvailable(this))
            {
                Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                Intent myIntent = new Intent(this, Hub.class);
                startActivity(myIntent);
            }
        }
        else if(!showToast)
        {
            Toast.makeText(this, "Multijogador brevemente!", Toast.LENGTH_SHORT).show();
            showToast = true;
        }*/


        Intent myIntent = new Intent(this, Hub.class);
        startActivity(myIntent);

    }

    public void pressMyAds(View v)
    {
        Intent myIntent = new Intent(this, SavedAds.class);
        startActivity(myIntent);
    }

    public void pressSettings(View v)
    {
        Intent myIntent = new Intent(this, SettingsIntent.class);
        startActivity(myIntent);
    }
}
