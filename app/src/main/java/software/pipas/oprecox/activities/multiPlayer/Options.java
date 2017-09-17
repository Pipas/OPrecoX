package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

/**
 * Created by nuno_ on 22-Aug-17.
 */

public class Options extends AppCompatActivity
{
    private String roomName;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_options);

        Intent intent = getIntent();
        this.roomName = intent.getExtras().getString(getResources().getString(R.string.roomName));

        initiateCustomFonts();
    }

    private void initiateCustomFonts()
    {
        TextView optionsTitleTextView = (TextView)findViewById(R.id.optionsTitleTextView);
        TextView gameTypeButtonTextView = (TextView)findViewById(R.id.gameTypeButtonTextView);
        TextView gameSizeButtonTextView = (TextView)findViewById(R.id.gameSizeButtonTextView);
        TextView categoriesButtonTextView = (TextView)findViewById(R.id.categoriesButtonTextView);

        TextView gameTypeTooltip = (TextView)findViewById(R.id.gameTypeTooltip);
        TextView gameSizeTooltip = (TextView)findViewById(R.id.gameSizeTooltip);

        TextView roomNameEdit = (TextView) findViewById(R.id.roomNameEdit);
        roomNameEdit.setText(this.roomName);

        CustomFontHelper.setCustomFont(roomNameEdit, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(optionsTitleTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameTypeButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameSizeButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(categoriesButtonTextView, "font/antipastopro-demibold.otf", getBaseContext());
        CustomFontHelper.setCustomFont(gameTypeTooltip, "font/Comfortaa_Thin.ttf", getBaseContext());
        CustomFontHelper.setCustomFont(gameSizeTooltip, "font/Comfortaa_Thin.ttf", getBaseContext());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    public void onOkPressed(View v)
    {
        this.updateName();
        this.setResult();
        finish();
    }

    public void onCancelPressed(View v)
    {
        finish();
    }

    private void updateName()
    {
        TextView roomNameEdit = (TextView) findViewById(R.id.roomNameEdit);
        String roomNameNew = roomNameEdit.getText().toString();

        if(roomNameNew.length() > 0)
        {
            this.roomName = roomNameNew;
        }
    }

    private void setResult()
    {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.roomName), this.roomName);
        setResult(RESULT_OK, intent);
    }
}
