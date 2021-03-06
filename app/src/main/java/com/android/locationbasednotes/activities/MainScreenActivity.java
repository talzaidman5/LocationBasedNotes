package com.android.locationbasednotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.firebase.FirebaseManager;
import com.android.locationbasednotes.fragment.ListModeFragment;
import com.android.locationbasednotes.fragment.MapModeFragment;
import com.android.locationbasednotes.utils.IDBManager;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class MainScreenActivity extends AppCompatActivity {
    private Button activity_main_BTN_signout;
    private FloatingActionButton activity_main_BTN_createNewNote;
    private MySheredP msp;
    private BottomNavigationView activity_main_NGV_navigationMenu;
    private User currentUser;
    private TextView activity_main_TXT_noNotes;
    protected IDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        findViews();
        getSupportActionBar().hide();

        showDefaultFragment();
        initData();
        getFromMSP();
        handleEmptyNotesList();

        activity_main_NGV_navigationMenu.setOnNavigationItemSelectedListener(navListener);

        activity_main_BTN_createNewNote.setOnClickListener(view -> startNewActivity(NoteScreenActivity.class));
        activity_main_BTN_signout.setOnClickListener(view -> openDialogSignOut(view));

    }

    private void openDialogSignOut(View view) {
        new AlertDialog.Builder(view.getContext(),R.style.AppCompatAlertDialogStyle)
                .setTitle(view.getContext().getString(R.string.signup))
                .setMessage(view.getContext().getString(R.string.are_you_sure_signup))

                .setPositiveButton(view.getContext().getString(R.string.yes), (dialog, which) -> {
                    dbManager.signOut();
                    finish();
                    startNewActivity(AuthenticateActivity.class);
                })
                .setNegativeButton(view.getContext().getString(R.string.no), null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void startNewActivity(Class<?> newActivity) {
        startActivity(new Intent(getApplicationContext(), newActivity));
    }

    private void handleEmptyNotesList() {
        if(currentUser.getNoteList()!=null) {
            if (currentUser.getNoteList().size() > 0)
                activity_main_TXT_noNotes.setVisibility(View.INVISIBLE);
            else {
                activity_main_TXT_noNotes.setText("No Notes");
                activity_main_TXT_noNotes.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initData() {
        msp = new MySheredP(this);
        dbManager = FirebaseManager.GetInstance();
    }

    private void showDefaultFragment() {
        MapModeFragment mapModeFragment = new MapModeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_FEM_frameLayout, mapModeFragment);
        transaction.commit();
    }

    private void findViews() {
        activity_main_BTN_createNewNote = findViewById(R.id.activity_main_BTN_createNewNote);
        activity_main_BTN_signout = findViewById(R.id.activity_main_BTN_logout);
        activity_main_NGV_navigationMenu = findViewById(R.id.activity_main_NGV_navigationMenu);
        activity_main_TXT_noNotes = findViewById(R.id.activity_main_TXT_noNotes);
    }

    private void getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
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
    };

}