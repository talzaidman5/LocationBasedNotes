package com.android.locationbasednotes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.locationbasednotes.ListModeFragment;
import com.android.locationbasednotes.MapModeFragment;
import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class MainScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button activity_main_BTN_logout;
    private FloatingActionButton activity_main_BTN_createNewNote;
    private MySheredP msp;
    private BottomNavigationView activity_main_NGV_navigationMenu;
    private User currentUser;
    private TextView activity_main_TXT_noNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        findViews();
        getSupportActionBar().hide();

        showDefaultFragment();
        initData();

        getFromMSP();

        if (currentUser.getNoteList() != null)
            activity_main_TXT_noNotes.setVisibility(View.INVISIBLE);
        else
        {
            activity_main_TXT_noNotes.setText("No Notes");
            activity_main_TXT_noNotes.setVisibility(View.VISIBLE);
        }
            activity_main_NGV_navigationMenu.setOnNavigationItemSelectedListener(navListener);
        activity_main_BTN_createNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NoteScreenActivity.class));
            }
        });

        activity_main_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

    }

    private void initData() {
        msp = new MySheredP(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void showDefaultFragment() {
        ListModeFragment listModeFragment = new ListModeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_FEM_frameLayout, listModeFragment);
        transaction.commit();
    }

    private void findViews() {
        activity_main_BTN_createNewNote = findViewById(R.id.activity_main_BTN_createNewNote);
        activity_main_BTN_logout = findViewById(R.id.activity_main_BTN_logout);
        activity_main_NGV_navigationMenu = findViewById(R.id.activity_main_NGV_navigationMenu);
        activity_main_TXT_noNotes = findViewById(R.id.activity_main_TXT_noNotes);

    }


    private void getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.list:
                    selectedFragment = new ListModeFragment();
                    break;
                case R.id.map:
                    selectedFragment = new MapModeFragment();
                    break;

            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_FEM_frameLayout, selectedFragment)
                    .commit();
            return true;
        }
        };

    }

