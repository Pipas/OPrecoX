package software.pipas.oprecox.Menus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import software.pipas.oprecox.Menus.SinglePlayerOptions;
import software.pipas.oprecox.R;

import static software.pipas.oprecox.R.string.categories;

public class MainMenu extends AppCompatActivity
{
    LinearLayout submenumultiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        submenumultiplayer = (LinearLayout) findViewById(R.id.submenumultiplayer);
    }

    @Override
    public void onBackPressed()
    {

    }

    public void startSinglePlayerGame(View v)
    {
        Intent myIntent = new Intent(this, SinglePlayerOptions.class);
        startActivity(myIntent);
    }

    public void toggleMultiplayerGame(View v)
    {
        if(submenumultiplayer.getVisibility() == View.VISIBLE)
            submenumultiplayer.setVisibility(View.GONE);
        else
            submenumultiplayer.setVisibility(View.VISIBLE);

    }

    public void createMultiplayer(View v)
    {
        Intent myIntent = new Intent(this, MultiPlayerOptions.class);
        startActivity(myIntent);
        submenumultiplayer.setVisibility(View.GONE);
    }

    public void connectMultiplayer(View v)
    {
        Intent myIntent = new Intent(this, MultiPlayerConnect.class);
        startActivity(myIntent);
        submenumultiplayer.setVisibility(View.GONE);
    }

    public void pressSettings(View v)
    {
        Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
    }
}
