package com.android.locationbasednotes;

import java.util.Calendar;
import java.util.Date;

public class Note {

    private Date date;
    private String title;
    private String body;

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
        this.date = Calendar.getInstance().getTime();

    }
}
