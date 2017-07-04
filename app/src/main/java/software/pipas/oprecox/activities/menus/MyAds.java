package software.pipas.oprecox.activities.menus;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.AddListAdapter;
import software.pipas.oprecox.modules.dataType.AdPreview;
import software.pipas.oprecox.modules.database.DatabaseHandler;

public class MyAds extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adds);

        DatabaseHandler database = new DatabaseHandler(this);
        database.open();
        //database.createAd("Caralhos", "18 no projecto de compiladores", BitmapFactory.decodeResource(getResources(), R.drawable.others));

        ListView myAddsList = (ListView) findViewById(R.id.myAddsList);

        ArrayList<AdPreview> ads = database.getAllComments();

        AddListAdapter addListAdapter = new AddListAdapter(ads, getApplicationContext(), getContentResolver());
        myAddsList.setAdapter(addListAdapter);

        database.close();
    }
}
