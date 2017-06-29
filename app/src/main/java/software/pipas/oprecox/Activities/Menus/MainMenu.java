package software.pipas.oprecox.activities.menus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.singlePlayer.Lobby;
import software.pipas.oprecox.modules.categories.Categories;
import software.pipas.oprecox.util.Util;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences sharedPref = getSharedPreferences("gameSettings", MODE_PRIVATE);
        String c = sharedPref.getString("categories", null);
        if(c != null)
            Categories.selectFromString(c);
        else
            Categories.selectAll();
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
        /*
        count++;
        if(count >= 10)
        {
            Intent myIntent = new Intent(this, software.pipas.oprecox.activities.multiPlayer.Lobby.class);
            startActivity(myIntent);
        }
        else
            Toast.makeText(this, "Multiplayer changes coming soon!", Toast.LENGTH_SHORT).show();
            */

        /*
        if(!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
            return;
        }
        */

        Intent myIntent = new Intent(this, software.pipas.oprecox.activities.multiPlayer.Lobby.class);
        startActivity(myIntent);

    }

    public void pressSettings(View v)
    {
        Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
    }
}
