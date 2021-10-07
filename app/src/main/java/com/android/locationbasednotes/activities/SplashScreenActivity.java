package com.android.locationbasednotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.android.locationbasednotes.firebase.FirebaseManager;
import com.android.locationbasednotes.firebase.OnUserFetchedCallback;
import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.IDBManager;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.gson.Gson;

public class SplashScreenActivity extends AppCompatActivity {
    private Gson gson = new Gson();
    private String currentUserID;
    private MySheredP msp;
    protected IDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        dbManager = FirebaseManager.GetInstance();

        InitData();

        if (currentUserID != null)
            dbManager.readFromDB(currentUserID, this, new OnUserFetchedCallback() {
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
        currentUserID = dbManager.getCurrentUserIDFromDB();
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