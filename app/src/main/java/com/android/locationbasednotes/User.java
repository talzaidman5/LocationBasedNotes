package com.android.locationbasednotes;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private List<Note> noteList;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.noteList = new ArrayList<>();
    }
}
