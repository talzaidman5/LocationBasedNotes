package com.android.locationbasednotes.firebase;

import com.android.locationbasednotes.data.User;

public interface FirebaseManagerCallback {
    public void OnUserFetched(User user);
}
