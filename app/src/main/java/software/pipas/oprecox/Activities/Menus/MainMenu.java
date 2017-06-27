package software.pipas.oprecox.Activities.Menus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.Activities.Multiplayer.Lobby;
import software.pipas.oprecox.R;

public class MainMenu extends AppCompatActivity
{
    private LinearLayout submenumultiplayer;
    private LinearLayout submenuname;
    private boolean hasName = false;
    private EditText nameinput;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        submenumultiplayer = (LinearLayout) findViewById(R.id.submenumultiplayer);
        submenuname = (LinearLayout) findViewById(R.id.submenuname);

        nameinput = (EditText) findViewById(R.id.nameinput);
        nameinput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        initiateEditTextListener();



        getSharedPreferences("gameSettings", MODE_PRIVATE).edit().clear().commit();
        SharedPreferences sharedPref = getSharedPreferences("gameSettings", MODE_PRIVATE);
        String name = sharedPref.getString("name", null);
        if(name != null)
            hasName = true;
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

    public void createLobby(View v)
    {
        Intent myIntent = new Intent(this, Lobby.class);
        startActivity(myIntent);
    }

    public void toggleMultiplayerGame(View v)
    {
        if(hasName)
        {
            if(submenumultiplayer.getVisibility() == View.VISIBLE)
                submenumultiplayer.setVisibility(View.GONE);
            else
                submenumultiplayer.setVisibility(View.VISIBLE);
        }
        else
        {
            if(submenuname.getVisibility() == View.VISIBLE)
                submenuname.setVisibility(View.GONE);
            else
                submenuname.setVisibility(View.VISIBLE);
        }
    }

    public void submitName(View v)
    {
        submitName();
    }

    private void submitName()
    {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
        String name;
        name = nameinput.getText().toString();
        if(name.isEmpty() || name.length() >= 30 || name.length() < 3 || name == null)
            invalidName();
        else
        {
            SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
            editor.putString("name", name);
            editor.apply();
            hasName = true;
            submenuname.setVisibility(View.GONE);
            submenumultiplayer.setVisibility(View.VISIBLE);
        }
    }

    private void invalidName()
    {
        Toast.makeText(this, "Nome inválido", Toast.LENGTH_SHORT).show();
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

    private void initiateEditTextListener()
    {
        nameinput.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitName();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}
