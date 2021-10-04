package com.android.locationbasednotes;

import java.util.Calendar;
import java.util.Date;

public class Note {

    private Date date;
    private String title;
    private String body;

    public Note(){}

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
        this.date = Calendar.getInstance().getTime();

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
