package com.android.locationbasednotes;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String Uid;
    private List<Note> noteList;

    public User( String email, String password,String uid) {
        this.email = email;
        this.password = password;
        this.noteList = new ArrayList<>();
        this.Uid = uid;
    }
    public User(){}
    public User(String userString) {createUserFromString(userString); }

    private  User createUserFromString(String data) {
        return new Gson().fromJson(data, User.class);
    }

        public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
    public void addToNoteList(Note note){
        if(this.noteList == null)
            this.noteList = new ArrayList<>();
        this.noteList.add(note);
    }
}