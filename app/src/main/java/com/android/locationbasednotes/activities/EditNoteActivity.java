package com.android.locationbasednotes.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.android.locationbasednotes.firebase.OnUserFetchedUriCallback;
import com.android.locationbasednotes.R;
import com.bumptech.glide.Glide;

public class EditNoteActivity extends NoteScreenActivity {
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        currentNote = getNoteFromMSP();
        currentUser = getUserFromMSP();

        setNoteData();

        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataActivity(false,activity_note_screen_EDT_title.getEditText().getText().toString(),
                        activity_note_screen_EDT_body.getEditText().getText().toString());
                dbManager.writeToDB(currentUser);
                Toast.makeText(getApplicationContext(), "Updated note successfully", Toast.LENGTH_LONG).show();
            }
        });
        activity_note_screen_BTN_uploadImage.setOnClickListener(v -> getImage());

        activity_note_screen_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkField(activity_note_screen_EDT_title.getEditText()) && checkField(activity_note_screen_EDT_body.getEditText())) {
                    saveNewData();
                    dbManager.writeToDB(currentUser);
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                    if(isAddImage) {
                        dbManager.saveImageInDB(currentNote,uri,currentUser);
                    }
                }
            }
        });
    }

    private void updateDataActivity(boolean enabled, String title, String body) {
        activity_note_screen_EDT_title.setEnabled(enabled);
        activity_note_screen_EDT_body.setEnabled(enabled);

        currentUser.getNote(currentNote.getID()).setTitle(title);
        currentUser.getNote(currentNote.getID()).setBody(body);
    }

    private void saveNewData() {
        saveImage(currentUser.getNote(currentNote.getID()));
        currentUser.getNote(currentNote.getID()).setImage(true);
        currentUser.getNote(currentNote.getID()).setTitle(activity_note_screen_EDT_title.getEditText().getText().toString());
        currentUser.getNote(currentNote.getID()).setBody(activity_note_screen_EDT_body.getEditText().getText().toString());
    }

    private void deleteNote() {

        dbManager.deleteImageFromDB(currentUser,currentNote,"Note deleted");
        currentUser.deleteNote(currentNote);
        dbManager.writeToDB(currentUser);
    }

    private void setNoteData() {

        updateDataActivity(true,currentNote.getTitle(),currentNote.getBody());
        activity_note_screen_TXT_title.setText(getString(R.string.editNote));
        activity_note_screen_EDT_body.getEditText().setText(currentNote.getBody());
        activity_note_screen_EDT_title.getEditText().setText(currentNote.getTitle());
        if (currentNote.isImage())
            downloadImage();
        else
            activity_note_screen_IMG_image.setVisibility(View.INVISIBLE);

    }

    private void downloadImage() {
        getUserFromMSP();
        dbManager.downloadImageFromDB(currentNote, new OnUserFetchedUriCallback() {
            @Override
            public void OnUserFetched(Uri uri) {
                isAddImage = true;
                Glide
                        .with(getApplicationContext())
                        .load(uri)
                        .into(activity_note_screen_IMG_image);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activity_note_screen_IMG_image.setVisibility(View.VISIBLE);
        Glide
                .with(getApplicationContext())
                .load(data.getData())
                .into(activity_note_screen_IMG_image);
        uri = data.getData();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Your code here
        return super.dispatchTouchEvent(ev);
    }
}