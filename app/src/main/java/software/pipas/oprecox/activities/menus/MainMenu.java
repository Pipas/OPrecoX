package software.pipas.oprecox.activities.menus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.Hub;
import software.pipas.oprecox.activities.singlePlayer.Lobby;
import software.pipas.oprecox.modules.network.Announcer;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;
    private Announcer announcer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initiateCustomFonts();

        //ANNOUNCER
        //this.startAnnouncer();
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

    private void initiateCustomFonts()
    {
        TextView singleplayerButtonTextView = (TextView)findViewById(R.id.singleplayerButtonTextView);
        TextView multiplayerButtonTextView = (TextView)findViewById(R.id.multiplayerButtonTextView);
        TextView savedAdsButtonTextView = (TextView)findViewById(R.id.savedAdsButtonTextView);

        Typeface Comfortaa_Bold = Typeface.createFromAsset(getAssets(),  "font/Comfortaa_Bold.ttf");

        singleplayerButtonTextView.setTypeface(Comfortaa_Bold);
        multiplayerButtonTextView.setTypeface(Comfortaa_Bold);
        savedAdsButtonTextView.setTypeface(Comfortaa_Bold);
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
        Intent myIntent = new Intent(this, SavedAds.class);
        startActivity(myIntent);
    }

    public void pressSettings(View v)
    {
        Intent myIntent = new Intent(this, SettingsIntent.class);
        startActivity(myIntent);
    }

    public void startAnnouncer()
    {
        /*ImageView imageView = (ImageView) findViewById(R.id.announcer);
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
        }*/
    }


}
