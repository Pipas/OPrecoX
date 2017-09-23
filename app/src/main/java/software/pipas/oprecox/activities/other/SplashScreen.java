package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import software.pipas.oprecox.activities.menus.MainMenu;
import software.pipas.oprecox.modules.categories.CategoryHandler;

public class SplashScreen extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        CategoryHandler.checkIfRestart(this);

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
