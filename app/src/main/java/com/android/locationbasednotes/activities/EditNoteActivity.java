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

        initNoteData();

        activity_note_screen_BTN_uploadImage.setOnClickListener(v -> getImage());

        activity_note_screen_BTN_delete.setOnClickListener(v -> deleteNote());
        activity_note_screen_BTN_save.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        if (checkField(activity_note_screen_EDT_title.getEditText()) && checkField(activity_note_screen_EDT_body.getEditText())) {
            saveNewData();
            if(isAddImage)
                dbManager.saveImageInDB(currentNote,uri,currentUser);
            finish();
            startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
        }
    }

    private void updateDataActivity(boolean enabled, String title, String body) {
        activity_note_screen_EDT_title.setEnabled(enabled);
        activity_note_screen_EDT_body.setEnabled(enabled);

        currentUser.getNote(currentNote.getID()).setTitle(title);
        currentUser.getNote(currentNote.getID()).setBody(body);
    }

    private void saveNewData() {

        updateDataActivity(false,activity_note_screen_EDT_title.getEditText().getText().toString(),
                activity_note_screen_EDT_body.getEditText().getText().toString());
        saveImage(currentUser.getNote(currentNote.getID()));
        currentUser.getNote(currentNote.getID()).setImage(true);
        dbManager.writeToDB(currentUser);

        Toast.makeText(getApplicationContext(), getString(R.string.updateNote), Toast.LENGTH_LONG).show();
    }

    private void deleteNote() {
        dbManager.deleteImageFromDB(currentUser,currentNote,"Note deleted");
        currentUser.deleteNote(currentNote);
        dbManager.writeToDB(currentUser);
    }

    private void initNoteData() {
        updateDataActivity(true,currentNote.getTitle(),currentNote.getBody());
        activity_note_screen_TXT_title.setText(getString(R.string.editNote));
        activity_note_screen_EDT_body.getEditText().setText(currentNote.getBody());
        activity_note_screen_EDT_title.getEditText().setText(currentNote.getTitle());
        isAddImage = false;
        if (currentNote.isImage()) {
            activity_note_screen_PRG_progressImage.setVisibility(View.VISIBLE);
            downloadImage();
        }
        else
            activity_note_screen_IMG_image.setVisibility(View.INVISIBLE);

    }

    private void downloadImage() {
        getUserFromMSP();
        dbManager.downloadImageFromDB(currentNote, uri -> {
            glideFunction(getApplicationContext(), uri, activity_note_screen_IMG_image);
            activity_note_screen_PRG_progressImage.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activity_note_screen_IMG_image.setVisibility(View.VISIBLE);
        glideFunction(getApplicationContext(), data.getData(), activity_note_screen_IMG_image);
        uri = data.getData();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}