package software.pipas.oprecox.activities.multiPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Player;

import software.pipas.oprecox.R;

/**
 * Created by nuno_ on 22-Jun-17.
 */

public class Hub extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private final int MAX_RETRIES = 1;
    private GoogleApiClient mGoogleApiClient;
    private int countConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        countConnect = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_hub);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);

        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();

        TextView displayName = (TextView) findViewById(R.id.displayName);
        displayName.setText(player.getDisplayName());

        TextView realName = (TextView) findViewById(R.id.realName);
        realName.setText(player.getName());


        ImageView imageView = (ImageView) findViewById(R.id.playerImage);
        ImageManager imageManager = ImageManager.create(this);
        imageManager.loadImage(imageView, player.getHiResImageUri());


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        this.startUserRetryRequest(connectionResult, this);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {

    }

    private void startUserRetryRequest(final ConnectionResult connectionResult, final Activity activity)
    {

        if(countConnect < MAX_RETRIES)
        {
            countConnect++;
            try
            {
                connectionResult.startResolutionForResult(activity, connectionResult.getErrorCode());
            }
            catch (Exception e)
            {

            }
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //builder.setCancelable(false);
        builder.setTitle("Connection Failed!");
        builder.setMessage("Do you wish to retry?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            connectionResult.startResolutionForResult(activity, connectionResult.getErrorCode());
                        }
                        catch (Exception e)
                        {

                        }
                    }
                });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                });

        builder.create();
        builder.show();
    }


}
