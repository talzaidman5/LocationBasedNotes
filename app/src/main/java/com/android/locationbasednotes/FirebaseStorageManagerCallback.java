package com.android.locationbasednotes;

import android.net.Uri;

import com.android.locationbasednotes.data.User;

public interface FirebaseStorageManagerCallback {
    public void OnUserFetched(Uri uri);
}
