package software.pipas.oprecox.Menus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import software.pipas.oprecox.Categories;
import software.pipas.oprecox.GameActivity;
import software.pipas.oprecox.R;

import java.util.ArrayList;

public class SinglePlayerOptions extends AppCompatActivity
{

    Categories categories = new Categories();
    ProgressDialog mProgressDialog;

    ArrayList<String> urls = new ArrayList<String>();
    int count;
    int NGUESSES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_options);

        setTitle(R.string.gameoptions);

        categories.selectAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 3)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ArrayList<String> selected = data.getStringArrayListExtra("categories");
                categories.setSelected(selected);
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
        else if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                NGUESSES = data.getIntExtra("NGUESSES", 10);
                TextView numberGuessesTooltip = (TextView) findViewById(R.id.numberguessestooltip);
                numberGuessesTooltip.setText(Integer.toString(NGUESSES));
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }

    public void startGame(View v)
    {
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("urls", urls);
        myIntent.putExtra("NGUESSES", NGUESSES);
        myIntent.putExtra("categories", categories.getSelected());
        startActivity(myIntent);
    }

    public void endAsyncTask(String s)
    {
        urls.add(s);
        count--;
        if(count <= 0)
        {
            mProgressDialog.dismiss();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("urls", urls);
            myIntent.putExtra("NGUESSES", NGUESSES);
            myIntent.putExtra("categories", categories.getSelected());
            startActivity(myIntent);
        }
    }

    public void selectCategory(View v)
    {
        Intent myIntent = new Intent(this, CategoryChooser.class);
        myIntent.putExtra("categories", categories.getSelected());
        startActivityForResult(myIntent, 3);
    }

    public void selectGuessNumber(View v)
    {
        Intent myIntent = new Intent(this, NGuessesChooser.class);
        startActivityForResult(myIntent, 2);
    }
}
