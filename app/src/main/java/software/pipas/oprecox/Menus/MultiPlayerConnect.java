package software.pipas.oprecox.Menus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import software.pipas.oprecox.GameActivity;
import software.pipas.oprecox.R;

public class MultiPlayerConnect extends AppCompatActivity
{
    private EditText codeInput;
    private DatabaseReference mDatabase;
    private ArrayList<String> urls = new ArrayList<>();
    private String code;
    private Boolean validCode = false;
    Context context = this;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_connect);

        setTitle("Conectar a um jogo");

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "signInAnonymously:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("FIREBASE", "signInAnonymously", task.getException());
                            Toast.makeText(MultiPlayerConnect.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        codeInput = (EditText) findViewById(R.id.codeInput);
        codeInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        codeInput.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(4)});

        initiateEditTextListener();
    }

    public void playGame(View v)
    {
        Intent myIntent = new Intent(this, GameActivity.class);
        myIntent.putExtra("urls", urls);
        myIntent.putExtra("NGUESSES", urls.size());
        myIntent.putExtra("gameType", true);
        myIntent.putExtra("roomCode", code);
        startActivity(myIntent);
    }

    public void verifyCode(View v)
    {
        verifyCode();
    }

    private void verifyCode()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("A procurar jogo");
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        code = codeInput.getText().toString();
        if(code.isEmpty() && code.length() != 4 && code == null)
            invalidCode();
        else
        {
            mDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(code).exists()) {
                        for (DataSnapshot areaSnapshot : dataSnapshot.child(code).child("urls").getChildren()) {
                            String areaName = areaSnapshot.getValue(String.class);
                            urls.add(areaName);
                        }
                        validCode();
                    } else
                        invalidCode();
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Toast.makeText(MultiPlayerConnect.this, "Erro a carregar jogo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void invalidCode()
    {
        Toast.makeText(this,"Código inválido", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
    }

    private void validCode()
    {
        if(validCode)
            return;
        validCode = true;
        mProgressDialog.dismiss();
        togglePlayButton();
    }

    private void togglePlayButton()
    {
        View fillerView = findViewById(R.id.fillerView);
        RelativeLayout playButton = (RelativeLayout) findViewById(R.id.playButton);
        if(fillerView.getVisibility() == View.VISIBLE)
        {
            fillerView.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }
        else
        {
            fillerView.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
        }

    }

    private void initiateEditTextListener()
    {
        codeInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(validCode)
                {
                    togglePlayButton();
                    validCode = false;
                }
            }
        });

        codeInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verifyCode();
                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}
