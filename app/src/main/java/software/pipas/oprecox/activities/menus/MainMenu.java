package software.pipas.oprecox.activities.menus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.Hub;
import software.pipas.oprecox.activities.other.BlockedApp;
import software.pipas.oprecox.activities.singlePlayer.Lobby;
import software.pipas.oprecox.modules.categories.Categories;
import software.pipas.oprecox.util.Settings;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getPreferences();

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
    public void onBackPressed()
    {

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
        Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
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
                finish();
            }
        }
        String c = sharedPref.getString("categories", null);
        if(c != null)
            Categories.selectFromString(c);
        else
            Categories.selectAll();
        Settings.setNewSavedAds(sharedPref.getInt("newSavedAds", 0));
    }
}
