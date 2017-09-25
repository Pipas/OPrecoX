package software.pipas.oprecox.activities.multiPlayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.singlePlayer.GameActivity;
import software.pipas.oprecox.modules.interfaces.ParsingCallingActivity;
import software.pipas.oprecox.modules.parsing.AsyncGetAd;
import software.pipas.oprecox.modules.parsing.AsyncGetAll;
import software.pipas.oprecox.modules.parsing.OlxParser;

/**
 * Created by nuno on 25-09-2017.
 */

public class PriceGuessGameMultiplayerActivity extends GameActivity implements ParsingCallingActivity
{
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<AsyncGetAd> asyncTasks;

    @Override
    public void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);

        gameSize = getIntent().getIntExtra(getString(R.string.S008_GAMESIZE), 10);

        //retirar urls

        this.startBroadcastReceiver();

        startDataParses();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    protected void startDataParses()
    {
        olxParser = new OlxParser();
        asyncTasks = new ArrayList<>();
        AsyncGetAd parsingAyncTask;
        for(int i = 2; i < gameSize; i++)
        {
            //retirar o null, necessita de urls
            parsingAyncTask = new AsyncGetAd(this, app, null, i, olxParser);
            parsingAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            asyncTasks.add(parsingAyncTask);
        }
    }

    @Override
    public void onBackPressed()
    {
        if(slideupGuesser.getState() == BottomSheetBehavior.STATE_EXPANDED)
            slideupGuesser.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
        {
            AlertDialog.Builder popup;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                popup = new AlertDialog.Builder(this, R.style.DialogTheme);
            else
                popup = new AlertDialog.Builder(this);
            popup.setTitle(getString(R.string.leavegame));
            popup.setMessage(getString(R.string.leavegamesub));
            popup.setCancelable(true);

            popup.setPositiveButton(
                    R.string.leave,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            leaveGame();
                        }
                    });

            popup.setNegativeButton(
                    R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = popup.create();
            alert.show();
        }
    }

    private void leaveGame()
    {
        for(AsyncGetAd asyncGetAd : this.asyncTasks)
        {
            asyncGetAd.cancel(true);
            finish();
        }
    }

    private void startBroadcastReceiver()
    {
        this.broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                handleIntentReceived(context, intent);
            }
        };

        IntentFilter filter = new IntentFilter(getString(R.string.S008));
        registerReceiver(this.broadcastReceiver, filter);

    }

    private void handleIntentReceived(Context context, Intent intent)
    {

    }
}
