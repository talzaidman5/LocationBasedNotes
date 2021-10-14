package com.android.locationbasednotes.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String password;
    private String Uid;
    private List<Note> noteList;
    private boolean loginAuth;

    public User( String email, String password,String uid) {
        this.email = email;
        this.password = password;
        this.noteList = new ArrayList<>();
        this.Uid = uid;
         this.loginAuth = false;

    }
    public User(){}
    public User(String userString) {createUserFromString(userString); }



    private  User createUserFromString(String data) {
        return new Gson().fromJson(data, User.class);
    }

        public String getUid() {
        return Uid;
    }

    public boolean isLoginAuth() {
        return loginAuth;
    }

    public void setLoginAuth(boolean loginAuth) {
        this.loginAuth = loginAuth;
    }


    public List<Note> getNoteList() {
        return noteList;
    }


    public void addToNoteList(Note note){
        if(this.noteList == null)
            this.noteList = new ArrayList<>();
        this.noteList.add(note);
    }
    public  Note getNote(String ID){
        for (Note note: this.getNoteList()) {
            if(note.getID().equals(ID))
                return note;
        }
        return null;
    }

    public void deleteNote(Note currentNote) {
        for (Note note : noteList) {
            if (note.getID().equals(currentNote.getID())) {
                noteList.remove(note);
                return;
            }
        }
    }
}
