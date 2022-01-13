package com.business.cryptosoar.Model;

public class ContractListModel {
    private String month;
    private String discriptio;
    private String value;


    public ContractListModel(String month, String discriptio, String value) {
        this.month = month;
        this.discriptio = discriptio;
        this.value = value;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDiscriptio() {
        return discriptio;
    }

    public void setDiscriptio(String discriptio) {
        this.discriptio = discriptio;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
