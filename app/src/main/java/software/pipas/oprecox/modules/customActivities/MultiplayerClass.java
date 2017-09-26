package software.pipas.oprecox.modules.customActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.net.DatagramPacket;

public abstract class MultiplayerClass extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    protected GoogleApiClient mGoogleApiClient;
    private boolean firstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        firstLaunch = true;

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
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
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

        if(firstLaunch)
        {
            firstLaunch = false;
            tryResolution(connectionResult, activity);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setTitle("Connection Failed!");
        builder.setMessage("Do you wish to retry?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                tryResolution(connectionResult, activity);
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

    private void tryResolution(final ConnectionResult connectionResult, final Activity activity)
    {
        try
        {
            connectionResult.startResolutionForResult(activity, connectionResult.getErrorCode());
        }
        catch (Exception e)
        {
            Log.d("HUB", "Error in tryResolution");
            e.printStackTrace();
        }
    }

    public void registerReceived(DatagramPacket packet)
    {

    }
}
