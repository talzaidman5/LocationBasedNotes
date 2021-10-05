package com.android.locationbasednotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button activity_main_BTN_createNewNote,activity_main_BTN_logout;
    private LinearLayout activity_main_LTN_allNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        FindViews();
        mAuth = FirebaseAuth.getInstance();

        activity_main_BTN_createNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), NoteScreenActivity.class));
            }
        });

        activity_main_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });
    }

    private void FindViews() {
        activity_main_BTN_createNewNote = findViewById(R.id.activity_main_BTN_createNewNote);
        activity_main_BTN_logout = findViewById(R.id.activity_main_BTN_logout);
        activity_main_LTN_allNotes = findViewById(R.id.activity_main_LTN_allNotes);
    }

}