package software.pipas.oprecox.Menus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import software.pipas.oprecox.Adds.AsyncGetURL;
import software.pipas.oprecox.Categories;
import software.pipas.oprecox.GameActivity;
import software.pipas.oprecox.R;


public class MultiPlayerOptions extends AppCompatActivity
{
    final private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private Categories categories = new Categories();
    private ProgressDialog mProgressDialog;

    ArrayList<String> onlineURLS = new ArrayList<String>();

    private ArrayList<String> urls = new ArrayList<String>();
    private int count;
    private int NGUESSES = 10;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String randomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_options);
        mAuth = FirebaseAuth.getInstance();

        setTitle(R.string.gameoptionsmultiplayer);

        SharedPreferences sharedPref = getSharedPreferences("gameSettings", MODE_PRIVATE);
        String c = sharedPref.getString("categories", null);
        if(c != null)
            categories.selectFromString(c);
        else
            categories.selectAll();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("FIREBASE", "signInAnonymously:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful())
                        {
                            Log.w("FIREBASE", "signInAnonymously", task.getException());
                            Toast.makeText(MultiPlayerOptions.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
                editor.putString("categories", categories.toString());
                editor.apply();
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
        if(!isNetworkAvailable())
        {
            Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("urls", urls);
        myIntent.putExtra("NGUESSES", NGUESSES);
        myIntent.putExtra("gameType", true);
        myIntent.putExtra("roomCode", randomCode);
        startActivity(myIntent);
    }

    public void generateGame(View v)
    {
        urls.clear();
        if(!isNetworkAvailable())
        {
            Toast.makeText(this, "Acesso à internet indisponivel", Toast.LENGTH_SHORT).show();
            return;
        }
        startProcessDialog();
        for(count = 0; count < NGUESSES; count++)
        {
            AsyncGetURL getURL = new AsyncGetURL(this, categories.getSelected());
            getURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void endAsyncTask(String s)
    {
        urls.add(s);
        count--;
        mProgressDialog.setProgress(NGUESSES - count);
        if(count <= 0)
        {
            checkFreeCode();
        }
    }

    public void continueEndAsyncTask()
    {
        mDatabase.child("games").child(randomCode).child("urls").setValue(urls);

        SharedPreferences sharedPref = getSharedPreferences("gameSettings", MODE_PRIVATE);
        String name = sharedPref.getString("name", null);
        mDatabase.child("games").child(randomCode).child("host").setValue(name);

        LinearLayout codeOutputLayout = (LinearLayout) findViewById(R.id.codeOutputLayout);
        codeOutputLayout.setVisibility(View.VISIBLE);

        TextView codeOutput = (TextView) findViewById(R.id.codeOutput);
        codeOutput.setText(randomCode);

        RelativeLayout startGame = (RelativeLayout) findViewById(R.id.startGame);
        RelativeLayout generateGame = (RelativeLayout) findViewById(R.id.generateGame);
        generateGame.setVisibility(View.GONE);
        startGame.setVisibility(View.VISIBLE);

        mProgressDialog.dismiss();
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

    private void startProcessDialog()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.loadingadds);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(NGUESSES);
        mProgressDialog.show();
    }

    private String generateCode()
    {
        int N = alphabet.length();
        Random r = new Random();
        String code = "";

        for (int i = 0; i < 4; i++)
        {
            code += alphabet.charAt(r.nextInt(N));
        }

        Log.d("CODE", code);
        return code;
    }

    private void checkFreeCode()
    {
        randomCode = generateCode();
        mDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(randomCode).exists())
                    checkFreeCode();
                else
                    continueEndAsyncTask();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
