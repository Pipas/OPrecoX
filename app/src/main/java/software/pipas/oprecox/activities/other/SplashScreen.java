package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.IOException;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.activities.menus.MainMenu;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.util.Settings;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getPreferences();

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
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
        Settings.setNewSavedAds(sharedPref.getInt("newSavedAds", 0));

    }
}
