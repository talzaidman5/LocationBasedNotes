package com.android.locationbasednotes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.authenticating.LoginActivity;
import com.android.locationbasednotes.data.User;
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
        getSupportActionBar().hide();

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
                putOnMSP();

                if (currentUser.isLoginAuth())
                    startNewActivity(MainScreenActivity.class);
                else
                    startNewActivity(LoginActivity.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startNewActivity(Class<?> newActivity) {
        finish();
        startActivity(new Intent(getApplicationContext(), newActivity));
    }

    private void putOnMSP() {
        String user = gson.toJson(currentUser);
        msp.putString(getString(R.string.UserKey), user);
    }
}