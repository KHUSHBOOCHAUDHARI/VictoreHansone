package com.business.victorehansone.Model;

public class SubscriptionModel {
    private String id;
    private String Title;
    private String Title_new;
    private String price;
    private String message;


    public SubscriptionModel(String id, String title, String title_new, String price, String message) {
        this.id = id;
        Title = title;
        Title_new = title_new;
        this.price = price;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTitle_new() {
        return Title_new;
    }

    public void setTitle_new(String title_new) {
        Title_new = title_new;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
