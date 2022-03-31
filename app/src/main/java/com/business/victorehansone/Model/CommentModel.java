package com.business.victorehansone.Model;

public class CommentModel {
    private String id;
    private String name;
    private String date;
    private String image;
    private String comment;

    public CommentModel(String id, String name, String date, String image, String comment) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.image = image;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
