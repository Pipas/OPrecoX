package software.pipas.oprecox.activities.menus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import software.pipas.oprecox.R;

public class SettingsIntent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
