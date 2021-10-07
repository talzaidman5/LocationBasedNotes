package com.android.locationbasednotes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.locationbasednotes.utils.MySheredP;

public class EditNoteActivity extends NoteScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        activity_note_screen_TXT_title.setText(getString(R.string.editNote));

       currentNote = getNoteFromMSP();
       currentUser = getUserFromMSP();
       UpdateNote();
       activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               activity_note_screen_EDT_title.setEnabled(true);
               activity_note_screen_EDT_body.setEnabled(true);

               currentUser.getNote(currentNote.getID()).setTitle(activity_note_screen_EDT_title.getText().toString());
               currentUser.getNote(currentNote.getID()).setBody(activity_note_screen_EDT_body.getText().toString());
               SaveToFirebase(currentUser);
               Toast.makeText(getApplicationContext(), "Updated note successfully", Toast.LENGTH_LONG).show();

           }
       });
        activity_note_screen_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DeleteNote();
            }
        });

    }

    private void DeleteNote() {
        currentUser.deleteNote(currentNote);
        SaveToFirebase(currentUser);
    }

    private void UpdateNote() {
        activity_note_screen_EDT_title.setText(currentNote.getTitle());
        activity_note_screen_EDT_body.setText(currentNote.getBody());

        activity_note_screen_EDT_title.setEnabled(true);
        activity_note_screen_EDT_body.setEnabled(true);


    }
}