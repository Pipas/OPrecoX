package software.pipas.oprecox.Menus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import software.pipas.oprecox.R;

import static android.R.attr.name;

public class MultiPlayerOptions extends AppCompatActivity
{
    @IgnoreExtraProperties
    public class Score
    {
        public String name;
        public int points;

        public Score() {}

        public Score(String n, int p)
        {
            name = n;
            points = p;
        }

        public String getName()
        {
            return name;
        }

        public int getPoints()
        {
            return points;
        }
    }

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_options);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("FIREBASE", "signInAnonymously", task.getException());
                            Toast.makeText(MultiPlayerOptions.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FIREBASE", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("FIREBASE", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void pressTest(View v)
    {
        String randomKey = "4DGT";
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("i really dont think you know");
        urls.add("afsdfaa");
        urls.add("adsfa");
        urls.add("ddddddd");
        urls.add("aasdfa");
        urls.add("aa");
        urls.add("before my world turned bluuuuuuuuuu");
        urls.add("32142134");
        mDatabase.child("games").child(randomKey).child("urls").setValue(urls);
        mDatabase.child("games").child(randomKey).child("scores").child("lmaf").child("name").setValue("Jose");
        int i = 322;
        mDatabase.child("games").child(randomKey).child("scores").child("lmaf").child("score").setValue(i);
    }


    public void pressTest2(View v)
    {
        final String randomKey = "4DGT";

        mDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final ArrayList<String> areas = new ArrayList<String>();
                String name = "";
                Long score = 0l;

                for (DataSnapshot areaSnapshot: dataSnapshot.child(randomKey).child("urls").getChildren())
                {
                    String areaName = areaSnapshot.getValue(String.class);
                    areas.add(areaName);
                }

                Log.d("TEST", areas.get(0));
                Log.d("TEST", areas.get(6));

                for (DataSnapshot areaSnapshot: dataSnapshot.child(randomKey).child("scores").getChildren())
                {
                    name = areaSnapshot.child("name").getValue(String.class);
                    score = (Long) areaSnapshot.child("score").getValue();
                }

                Log.d("TEST", String.format("%s - %d", name, score));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });;

    }
}
