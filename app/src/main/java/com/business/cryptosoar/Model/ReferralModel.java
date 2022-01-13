package com.business.cryptosoar.Model;

public class ReferralModel {
    private String id;
    private String name;
    private String referalcode;
    private String date;
    private String amount;

    public ReferralModel(String id, String name, String referalcode, String date, String amount) {
        this.id = id;
        this.name = name;
        this.referalcode = referalcode;
        this.date = date;
        this.amount = amount;
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

    public String getReferalcode() {
        return referalcode;
    }

    public void setReferalcode(String referalcode) {
        this.referalcode = referalcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
