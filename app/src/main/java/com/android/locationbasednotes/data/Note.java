package com.android.locationbasednotes.data;

import android.location.Location;
import android.net.Uri;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Note {

    private Date date;
    private String title;
    private String body;
    private List<Double> location;
    private String ID;
    private boolean isImage;

    public Note(){}

    public Note(String title, String body, List<Double>location) {
        this.title = title;
        this.body = body;
        this.date = Calendar.getInstance().getTime();
        this.location = location;
        this.ID = UUID.randomUUID().toString();
        this.isImage = false;

    }
    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
    public List<Double> getLocation() {
        return location;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public String getID() {
        return ID;
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
