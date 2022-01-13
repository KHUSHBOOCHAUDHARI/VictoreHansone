package com.business.cryptosoar.Model;

public class PaymentSetingListModel {
    private String id;
    private String name;
    private String Address;
    private String Lable;

    public PaymentSetingListModel(String id, String name, String address, String lable) {
        this.id = id;
        this.name = name;
        Address = address;
        Lable = lable;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLable() {
        return Lable;
    }

    public void setLable(String lable) {
        Lable = lable;
    }
}
