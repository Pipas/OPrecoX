package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import software.pipas.oprecox.R;

public class NGuessesChooser extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguesses_chooser);

        setTitle(R.string.guessnumber);
    }

    public void pressguess5(View v)
    {
        confirmSelection(5);
    }

    public void pressguess10(View v)
    {
        confirmSelection(10);
    }

    public void pressguess20(View v)
    {
        confirmSelection(20);
    }

    private void confirmSelection(int n)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("NGUESSES", n);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
