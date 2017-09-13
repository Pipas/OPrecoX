package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.IOException;

import software.pipas.oprecox.activities.menus.MainMenu;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.util.Settings;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent myIntent = getIntent();
        Boolean restart = myIntent.getBooleanExtra("restart", false);

        getPreferences();

        Settings.setDeviceDisplayMetrics(getResources().getDisplayMetrics());

        if(!restart)
        {
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
        }
        finish();
    }

    private void getPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences("gameSettings", MODE_PRIVATE);
        int version = sharedPref.getInt("lockVersion", Integer.MAX_VALUE);
        try
        {
            CategoryHandler.initiateFromXml(getApplicationContext().getAssets().open("categories.xml"), this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String c = sharedPref.getString("categories", null);
        if(c != null)
            CategoryHandler.selectFromString(c);
        else
            CategoryHandler.selectAll();

    }
}
