package com.android.locationbasednotes.utils;

import android.content.Context;
import android.net.Uri;

import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.firebase.OnUserFetchedCallback;
import com.android.locationbasednotes.firebase.OnUserFetchedUriCallback;
import com.android.locationbasednotes.firebase.OnUserSignCallback;

public interface IDBManager {

    public void readFromDB(String userID, Context activityContext, OnUserFetchedCallback callback);

    public void writeToDB(User user);
    public void deleteImageFromDB(User user, Note currentNote, String textOnSuccess);
    public void downloadImageFromDB(Note currentNote, OnUserFetchedUriCallback callback);
    public String getCurrentUserIDFromDB() ;
    public void signOut ();
    public void createUserWithEmailAndPassword(String email, String password, OnUserSignCallback onUserSignCallback);
    public void saveImageInDB(Note note, Uri fileUri,User user);
    public void signInWithEmailAndPassword(String email, String password, OnUserSignCallback onUserSignCallback);
    }
