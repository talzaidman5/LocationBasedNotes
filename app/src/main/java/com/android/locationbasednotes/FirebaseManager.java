package com.android.locationbasednotes;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.locationbasednotes.activities.MainActivity;
import com.android.locationbasednotes.activities.MainScreenActivity;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private FirebaseManager(){
        myRef = database.getReference(FIREBASE_REFERENCE);

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



}
