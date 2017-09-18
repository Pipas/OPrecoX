package software.pipas.oprecox.activities.multiPlayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.CategoryChooser;
import software.pipas.oprecox.activities.other.GameSizeChooser;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

public class Options extends AppCompatActivity
{
    private String roomName;
    private int gameSize;
    private TextView roomNameEdit;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby_options);

        Intent intent = getIntent();
        this.roomName = intent.getExtras().getString(getResources().getString(R.string.roomName));

        initiateCustomFonts();

        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onConfirmPressed();
            }
        });
    }

    private void initiateCustomFonts()
    {
        TextView optionsTitleTextView = (TextView)findViewById(R.id.optionsTitleTextView);
        TextView gameTypeButtonTextView = (TextView)findViewById(R.id.gameTypeButtonTextView);
        TextView gameSizeButtonTextView = (TextView)findViewById(R.id.gameSizeButtonTextView);
        TextView categoriesButtonTextView = (TextView)findViewById(R.id.categoriesButtonTextView);

        TextView gameTypeTooltip = (TextView)findViewById(R.id.gameTypeTooltip);
        TextView gameSizeTooltip = (TextView)findViewById(R.id.gameSizeTooltip);

        roomNameEdit = (TextView) findViewById(R.id.roomNameEdit);
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

    public void onConfirmPressed()
    {
        this.updateName();
        this.setResult();
        finish();
    }

    private void updateName()
    {
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

    public void selectCategory(View v)
    {
        Intent myIntent = new Intent(this, CategoryChooser.class);
        myIntent.putExtra("multiplayer", true);
        roomNameEdit.clearFocus();
        startActivity(myIntent);
    }

    public void selectGameSize(View v)
    {
        Intent myIntent = new Intent(this, GameSizeChooser.class);
        myIntent.putExtra("multiplayer", true);
        roomNameEdit.clearFocus();
        startActivityForResult(myIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gameSize = data.getIntExtra("gameSize", 10);
                TextView numberGuessesTooltip = (TextView) findViewById(R.id.gameSizeTooltip);
                numberGuessesTooltip.setText(Integer.toString(gameSize));
            }
        }
    }
}
