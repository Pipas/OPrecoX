package software.pipas.oprecox.activities.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import software.pipas.oprecox.R;

public class BlockedApp extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_app);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("Algo mudou no OLX contacte os developers ou actualize a aplicação");
    }

    @Override
    public void onBackPressed()
    {

    }
}
