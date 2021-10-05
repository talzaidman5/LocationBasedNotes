package com.android.locationbasednotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.locationbasednotes.utils.MySheredP;
import com.google.gson.Gson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private Gson gson = new Gson();
    private FirebaseUser currentUserFB;
    private MySheredP msp;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        InitData();

        if (currentUserFB != null) {
            ReadFromFirebase();


        } else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void InitData() {
        msp = new MySheredP(this);
        mAuth = FirebaseAuth.getInstance();
        currentUserFB = mAuth.getCurrentUser();
        myRef = database.getReference(getString(R.string.AllUsersFirebase));

    }

    private void ReadFromFirebase() {

        myRef.child(currentUserFB.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                if (currentUser.isLoginAuth()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                }
                else
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                putOnMSP();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ffff","fff");
            }
        });
    }

    private void putOnMSP() {
    //    currentUser.setUid(currentUserFB.getUid());
        String user = gson.toJson(currentUser);
        msp.putString(getString(R.string.UserKey), user);
    }
}