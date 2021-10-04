package com.android.locationbasednotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteScreenActivity extends AppCompatActivity {
    private Button activity_note_screen_BTN_delete;
    private EditText activity_note_screen_TXT_title,activity_note_screen_EDT_body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);
        FindViews();

        activity_note_screen_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewNote();
            }
        });
    }

    private void CreateNewNote() {
        Note note = new Note(activity_note_screen_TXT_title.getText().toString(),activity_note_screen_EDT_body.getText().toString());
        AddNoteToUser(note);
    }

    private void AddNoteToUser(Note note) {

    }

    private void FindViews() {
        activity_note_screen_BTN_delete = findViewById(R.id.activity_note_screen_BTN_delete);
    }
}