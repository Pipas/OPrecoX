package software.pipas.oprecox.activities.menus;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.multiPlayer.Hub;
import software.pipas.oprecox.activities.singlePlayer.Options;
import software.pipas.oprecox.modules.adapters.OptionsPopupAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.util.Settings;
import software.pipas.oprecox.util.Util;

public class MainMenu extends AppCompatActivity
{
    private int count = 0;
    private Boolean showToast = false;
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initiateCustomFonts();

        CategoryHandler.checkIfRestart(this);

        if(Settings.getGamesPlayed() >= 5 && Settings.getShowRateUs())
        {
            AlertDialog.Builder popup;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                popup = new AlertDialog.Builder(this, R.style.DialogTheme);
            else
                popup = new AlertDialog.Builder(this);

            popup.setTitle(getString(R.string.rateUs));
            popup.setMessage(getString(R.string.rateUsTooltip));
            popup.setCancelable(true);

            popup.setPositiveButton
                    (
                            R.string.rateUsYes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                    // To count with Play market backstack, After pressing back button,
                                    // to taken back to our application, we need to add following flags to intent.
                                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                    try {
                                        startActivity(goToMarket);
                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                    }
                                    Settings.setShowRateUs(false);
                                    SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
                                    editor.putBoolean("showRateUs", false);
                                    editor.apply();
                                    dialog.cancel();
                                }
                            });

            popup.setNegativeButton(
                    R.string.rateUsNo,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Settings.setShowRateUs(false);
                            SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
                            editor.putBoolean("showRateUs", false);
                            editor.apply();
                            dialog.cancel();
                        }
                    });

            popup.setNeutralButton(
                    R.string.rateUsLater,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = popup.create();
            alert.show();
        }

        initiatePressMoreButton();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder popup;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            popup = new AlertDialog.Builder(this, R.style.DialogTheme);
        else
            popup = new AlertDialog.Builder(this);

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

        CustomFontHelper.setCustomFont(singleplayerButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(multiplayerButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(savedAdsButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
    }

    public void pressSinglePlayer(View v)
    {
        Intent myIntent = new Intent(this, Options.class);
        startActivity(myIntent);
    }

    public void pressMultiPlayer(View v)
    {
        if(!Util.isNetworkAvailable(this))
        {
            Toasty.error(this, getString(R.string.interneterror), Toast.LENGTH_SHORT, true).show();
            return;
        }
        else
        {
            Intent myIntent = new Intent(this, Hub.class);
            startActivity(myIntent);
        }
    }

    private void initiatePressMoreButton()
    {
        ImageView pressMore = (ImageView) findViewById(R.id.pressMore);
        View anchorView = findViewById(R.id.anchorView);
        pressMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(listPopupWindow.isShowing())
                    listPopupWindow.dismiss();
                else
                    listPopupWindow.show();
            }
        });

        listPopupWindow = new ListPopupWindow(MainMenu.this);
        ArrayList<String> options = new ArrayList<>();
        options.add(getResources().getString( R.string.infoTitle));
        listPopupWindow.setAdapter(new OptionsPopupAdapter(options, getApplicationContext(), getContentResolver()));
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.setWidth((int) (100 * getResources().getDisplayMetrics().density + 0.5f));
        listPopupWindow.setHeight((int) (56 * options.size() * getResources().getDisplayMetrics().density + 0.5f));
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    Intent myIntent = new Intent(MainMenu.this, InfoActivity.class);
                    startActivity(myIntent);
                    listPopupWindow.dismiss();
                }
            }
        });
    }

    public void pressMyAds(View v)
    {
        Intent myIntent = new Intent(this, SavedAds.class);
        startActivity(myIntent);
    }
}
