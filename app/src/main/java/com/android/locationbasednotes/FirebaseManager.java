package com.android.locationbasednotes;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.locationbasednotes.activities.MainActivity;
import com.android.locationbasednotes.activities.MainScreenActivity;
import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

public class FirebaseManager { // Singleton object

    private static FirebaseManager instance;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private User currentUser;
    private MySheredP msp;
    protected Gson gson = new Gson();
    private Context context;
    private final String FIREBASE_REFERENCE = "AllUsers";
    protected StorageReference mStorageRef;

    private FirebaseManager(){
        myRef = database.getReference(FIREBASE_REFERENCE);
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public static FirebaseManager GetInstance(){
        if (instance == null)
            instance = new FirebaseManager();
        return instance;
    }

    public void readFromFirebase(FirebaseUser user, Context activityContext, FirebaseManagerCallback callback) {
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                context = activityContext;
                callback.OnUserFetched(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
        public void writeToFirebase(User user) {
            myRef.child(user.getUid()).setValue(user);
        }

public void saveImageInStorage(Note note, Uri fileUri, Context context) {

    mStorageRef.child(currentUser.getUid()).child(note.getID()).putFile(fileUri)
            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
        }
    });

}
public void deleteImageFromStorage(User user, Note currentNote, String textOnSuccess){
    mStorageRef.child(user.getUid()).child(currentNote.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(context, textOnSuccess, Toast.LENGTH_LONG).show();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
        }
    });
}
public void downloadImageFromStorage(Note currentNote, FirebaseManagerCallback callback){
    mStorageRef.child(currentUser.getUid()).child(currentNote.getID()).getDownloadUrl().
            addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//callback();
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


}
