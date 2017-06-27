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

public class MainMenu extends AppCompatActivity
{
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
        Intent myIntent = new Intent(this, software.pipas.oprecox.activities.multiPlayer.Lobby.class);
        startActivity(myIntent);
    }

    public void pressSettings(View v)
    {
        Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
    }
}
