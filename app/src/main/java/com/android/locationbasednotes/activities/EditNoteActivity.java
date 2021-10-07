package com.android.locationbasednotes.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.locationbasednotes.Adapter_Note;
import com.android.locationbasednotes.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

public class EditNoteActivity extends NoteScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        activity_note_screen_TXT_title.setText(getString(R.string.editNote));
        mStorageRef = FirebaseStorage.getInstance().getReference();

        currentNote = getNoteFromMSP();
        currentUser = getUserFromMSP();
        updateNote();

        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_note_screen_EDT_title.setEnabled(true);
                activity_note_screen_EDT_body.setEnabled(true);

                currentUser.getNote(currentNote.getID()).setTitle(activity_note_screen_EDT_title.getText().toString());
                currentUser.getNote(currentNote.getID()).setBody(activity_note_screen_EDT_body.getText().toString());
                saveToFirebase(currentUser);
                Toast.makeText(getApplicationContext(), "Updated note successfully", Toast.LENGTH_LONG).show();
            }
        });
        activity_note_screen_BTN_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });


        activity_note_screen_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(currentUser.getNote(currentNote.getID()));
                currentUser.getNote(currentNote.getID()).setImage(true);

                saveToFirebase(currentUser);
                finish();

                startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
            }
        });
    }



    private void deleteNote() {

        mStorageRef.child(currentUser.getUid()).child(currentNote.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
        currentUser.deleteNote(currentNote);
        saveToFirebase(currentUser);
    }

    private void updateNote() {
        activity_note_screen_EDT_title.setText(currentNote.getTitle());
        activity_note_screen_EDT_body.setText(currentNote.getBody());

        activity_note_screen_EDT_title.setEnabled(true);
        activity_note_screen_EDT_body.setEnabled(true);
        if (currentNote.isImage())
            downloadImage(activity_note_screen_IMG_image);
        else
            activity_note_screen_IMG_image.setBackgroundColor(getColor(R.color.noteWithoutImage));

    }

        private void downloadImage(ImageView activity_note_screen_IMG_image) {
            getUserFromMSP();
            mStorageRef.child(currentUser.getUid()).child(currentNote.getID()).getDownloadUrl().
                    addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide
                                    .with(getApplicationContext())
                                    .load(uri)
                                    .into(activity_note_screen_IMG_image);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                }
            });

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Glide
                .with(getApplicationContext())
                .load(data.getData())
                .into(activity_note_screen_IMG_image);

    }
}