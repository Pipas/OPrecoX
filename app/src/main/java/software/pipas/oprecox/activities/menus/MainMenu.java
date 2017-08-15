package software.pipas.oprecox.activities.menus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.Hub;
import software.pipas.oprecox.util.Settings;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Settings.setDeviceDisplayMetrics(getResources().getDisplayMetrics());


    }


    @Override
    public void onResume()
    {
        super.onResume();
        TextView newAdsDisplay = (TextView) findViewById(R.id.newAdsDisplay);
        if(Settings.getNewSavedAds() == 0)
            newAdsDisplay.setVisibility(View.GONE);
        else
            newAdsDisplay.setText(Integer.toString(Settings.getNewSavedAds()));


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
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

    public void pressSinglePlayer(View v)
    {
        Intent myIntent = new Intent(this, software.pipas.oprecox.activities.singlePlayer.Lobby.class);
        startActivity(myIntent);
    }

    public void pressMultiPlayer(View v)
    {
        /*
        count++;
        if(count >= 10)
        {
            Intent myIntent = new Intent(this, Hub.class);
            startActivity(myIntent);
        }
        else


        Toast.makeText(this, "Multiplayer changes coming soon!", Toast.LENGTH_SHORT).show();*/



        /*if(!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
            return;
        }

        */


        Intent myIntent = new Intent(this, Hub.class);
        startActivity(myIntent);

    }

    public void pressMyAds(View v)
    {
        Intent myIntent = new Intent(this, MyAds.class);
        startActivity(myIntent);
    }

    public void pressSettings(View v)
    {
        Intent myIntent = new Intent(this, SettingsIntent.class);
        startActivity(myIntent);
    }




}
