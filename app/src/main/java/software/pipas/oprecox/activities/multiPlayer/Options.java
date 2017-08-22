package software.pipas.oprecox.activities.multiPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import software.pipas.oprecox.R;

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

        TextView roomNameEdit = (TextView) findViewById(R.id.roomNameEdit);
        roomNameEdit.setText(this.roomName);
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
