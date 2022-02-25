package com.business.agrocoin.Model;

public class NotificationModel {
    private String id;
    private String title;
    private String date;
    private String message;


    public NotificationModel(String id, String title, String date, String message) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
