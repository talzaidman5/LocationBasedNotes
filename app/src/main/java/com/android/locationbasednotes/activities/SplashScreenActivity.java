package com.android.locationbasednotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.android.locationbasednotes.firebase.FirebaseManager;
import com.android.locationbasednotes.firebase.FirebaseManagerCallback;
import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.gson.Gson;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private Gson gson = new Gson();
    private FirebaseUser currentUserFB;
    private MySheredP msp;
    protected FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        firebaseManager = FirebaseManager.GetInstance();

        InitData();

        if (currentUserFB != null)
            firebaseManager.readFromFirebase(currentUserFB, this, new FirebaseManagerCallback() {
                @Override
                public void OnUserFetched(User user) {
                    putOnMSP(user);
                    if (user.isLoginAuth())
                        startNewActivity(MainScreenActivity.class);
                    else
                        startNewActivity(MainActivity.class);
                }
            });
        else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void InitData() {
        msp = new MySheredP(this);
        currentUserFB = firebaseManager.getCurrentUserAuth();
    }



    private void startNewActivity(Class<?> newActivity) {
        finish();
        startActivity(new Intent(getApplicationContext(), newActivity));
    }

    protected void putOnMSP(User userToSave) {
        msp = new MySheredP(getApplicationContext());
        String user = gson.toJson(userToSave);
        msp.putString(getApplication().getString(R.string.UserKey), user);
    }}