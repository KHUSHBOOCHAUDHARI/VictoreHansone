package com.business.victorehansone.Model;

public class SliderItems {
    //set to String, if you want to add image url from internet
    private String id;
    private String Image;
    private String VDH;
    private String Date;
    private String Comment;
    private String Headline;
    private String News;
    private String link;
    private String longnews;

    public SliderItems(String id, String image, String VDH, String date, String comment, String headline, String news, String link, String longnews) {
        this.id = id;
        Image = image;
        this.VDH = VDH;
        Date = date;
        Comment = comment;
        Headline = headline;
        News = news;
        this.link = link;
        this.longnews = longnews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getVDH() {
        return VDH;
    }

    public void setVDH(String VDH) {
        this.VDH = VDH;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getHeadline() {
        return Headline;
    }

    public void setHeadline(String headline) {
        Headline = headline;
    }

    public String getNews() {
        return News;
    }

    public void setNews(String news) {
        News = news;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLongnews() {
        return longnews;
    }

    public void setLongnews(String longnews) {
        this.longnews = longnews;
    }
}


