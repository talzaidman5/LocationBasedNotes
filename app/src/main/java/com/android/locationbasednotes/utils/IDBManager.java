package com.android.locationbasednotes.utils;

import android.content.Context;
import android.net.Uri;

import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.firebase.OnUserFetchedCallback;
import com.android.locationbasednotes.firebase.OnUserFetchedUriCallback;
import com.android.locationbasednotes.firebase.OnUserSignCallback;

public interface IDBManager {

     void readFromDB(String userID, Context activityContext, OnUserFetchedCallback callback);
     void writeToDB(User user);
     void deleteImageFromDB(User user, Note currentNote, String textOnSuccess);
     void downloadImageFromDB(Note currentNote, OnUserFetchedUriCallback callback);
     String getCurrentUserIDFromDB() ;
     void signOut ();
     void createUserWithEmailAndPassword(String email, String password, OnUserSignCallback onUserSignCallback);
     void saveImageInDB(Note note, Uri fileUri,User user);
     void signInWithEmailAndPassword(String email, String password, OnUserSignCallback onUserSignCallback);
    }
