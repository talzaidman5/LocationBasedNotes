package com.android.locationbasednotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.authenticating.LoginActivity;
import com.android.locationbasednotes.authenticating.SignupActivity;

public class MainActivity extends AppCompatActivity {
    private Button main_activity_BTN_login, main_activity_BTN_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        findViews();
        main_activity_BTN_login.setOnClickListener(v ->startNewActivity(LoginActivity.class));
        main_activity_BTN_signup.setOnClickListener(v ->  startNewActivity(SignupActivity.class));
    }

    private void startNewActivity(Class<?> newActivity) {
        startActivity(new Intent(getApplicationContext(), newActivity));
    }


    private void findViews() {
        main_activity_BTN_login = findViewById(R.id.main_activity_BTN_login);
        main_activity_BTN_signup = findViewById(R.id.main_activity_BTN_signup);
    }

}