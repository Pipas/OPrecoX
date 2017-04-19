package software.pipas.oprecox.Menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import software.pipas.oprecox.Menus.SinglePlayerOptions;
import software.pipas.oprecox.R;

import static software.pipas.oprecox.R.string.categories;

public class MainMenu extends AppCompatActivity
{
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startSinglePlayerGame(View v)
    {
        Intent myIntent = new Intent(this, SinglePlayerOptions.class);
        startActivity(myIntent);
    }

    public void startMultiplayerGame(View v)
    {
        Toast.makeText(this, getString(R.string.comingsoon), Toast.LENGTH_SHORT).show();
        count++;
        if(count == 10)
        {
            Intent myIntent = new Intent(this, MultiPlayerOptions.class);
            startActivity(myIntent);
        }
    }

    public void pressSettings(View v)
    {
        Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
    }
}
