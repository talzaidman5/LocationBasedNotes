package com.android.locationbasednotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.locationbasednotes.utils.MySheredP;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class NoteScreenActivity extends AppCompatActivity {
    private Button activity_note_screen_BTN_save,activity_note_screen_BTN_delete;
    private EditText activity_note_screen_EDT_title,activity_note_screen_EDT_body;
    private MySheredP msp;
    private User currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);
        FindViews();
        msp = new MySheredP(this);
        myRef = database.getReference(getString(R.string.AllUsersFirebase));

        getFromMSP();
        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewNote();
            }
        });
    }

    private void CreateNewNote() {
        Note note = new Note(activity_note_screen_EDT_title.getText().toString(),activity_note_screen_EDT_body.getText().toString());
        AddNoteToUser(note);
        Toast.makeText(getApplicationContext(), "New note successfully added!", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this, MainActivity.class));

    }

    private void AddNoteToUser(Note note) {
        currentUser.addToNoteList(note);
        SaveToFirebase(currentUser);
    }
    private void SaveToFirebase(User userToSave) {
        myRef.child(userToSave.getUid()).setValue(userToSave);

    }

    private void FindViews() {
        activity_note_screen_BTN_delete = findViewById(R.id.activity_note_screen_BTN_delete);
        activity_note_screen_BTN_save = findViewById(R.id.activity_note_screen_BTN_save);
        activity_note_screen_EDT_title = findViewById(R.id.activity_note_screen_EDT_title);
        activity_note_screen_EDT_body = findViewById(R.id.activity_note_screen_EDT_body);
    }
    private void getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
    }

}