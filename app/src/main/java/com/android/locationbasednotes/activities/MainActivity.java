package com.android.locationbasednotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.locationbasednotes.R;

public class MainActivity extends AppCompatActivity {
    private Button main_activity_BTN_login,main_activity_BTN_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        main_activity_BTN_login =  findViewById(R.id.main_activity_BTN_login);
        main_activity_BTN_signup =  findViewById(R.id.main_activity_BTN_signup);
        main_activity_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        main_activity_BTN_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });

    }
}