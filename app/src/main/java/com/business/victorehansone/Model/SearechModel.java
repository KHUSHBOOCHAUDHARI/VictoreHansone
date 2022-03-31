package com.business.victorehansone.Model;

public class SearechModel {
    private String id;
    private String title;
    private String VDH;

    public SearechModel(String id, String title, String VDH) {
        this.id = id;
        this.title = title;
        this.VDH = VDH;
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

    public String getVDH() {
        return VDH;
    }

    public void setVDH(String VDH) {
        this.VDH = VDH;
    }
}
