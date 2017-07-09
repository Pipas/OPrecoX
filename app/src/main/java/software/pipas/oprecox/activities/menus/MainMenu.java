package software.pipas.oprecox.activities.menus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.R;
import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.activities.multiPlayer.Hub;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.activities.singlePlayer.Lobby;
import software.pipas.oprecox.modules.categories.Categories;
import software.pipas.oprecox.modules.network.Announcer;
import software.pipas.oprecox.util.Settings;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;
    private Announcer announcer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getPreferences();

        Settings.setDeviceDisplayMetrics(getResources().getDisplayMetrics());

        //ANNOUNCER
        this.startAnnouncer();
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
        this.announcer.close();
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
        Intent myIntent = new Intent(this, Lobby.class);
        startActivity(myIntent);
    }

    public void pressMultiPlayer(View v)
    {

        count++;
        if(count >= 10)
        {
            Intent myIntent = new Intent(this, Hub.class);
            startActivity(myIntent);
        }
        else
            Toast.makeText(this, "Multiplayer changes coming soon!", Toast.LENGTH_SHORT).show();



        /*if(!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent myIntent = new Intent(this, Hub.class);
        startActivity(myIntent);*/

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

    private void getPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences("gameSettings", MODE_PRIVATE);
        Settings.setLocked(sharedPref.getBoolean("locked", false));
        int version = sharedPref.getInt("lockVersion", Integer.MAX_VALUE);
        if(Settings.isLocked())
        {
            if(version < BuildConfig.VERSION_CODE)
            {
                SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
                editor.putBoolean("locked", false);
                editor.apply();
            }
            else
            {
                Intent intent = new Intent(this, BlockedApp.class);
                startActivity(intent);
            }
        }
        String c = sharedPref.getString("categories", null);
        if(c != null)
            Categories.selectFromString(c);
        else
            Categories.selectAll();
        Settings.setNewSavedAds(sharedPref.getInt("newSavedAds", 0));
    }

    public void startAnnouncer()
    {
        ImageView imageView = (ImageView) findViewById(R.id.announcer);
        this.announcer = new Announcer(this.getApplicationContext());

        if(this.announcer.isValid())
        {
            imageView.setImageResource(R.drawable.shout_on);
            this.announcer.execute();
        }
        else
        {
            imageView.setImageResource(R.drawable.shout_off);
            Toast.makeText(this, "Cannot Announce", Toast.LENGTH_SHORT).show();
        }
    }


}
