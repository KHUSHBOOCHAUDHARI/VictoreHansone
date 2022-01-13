package com.business.cryptosoar.Model;

public class TransactionModel {
    private String id;
    private String title;

    private String total_amt;
    private String transaction_type;

    private String date;
    private String Status;
    private String type;

    public TransactionModel(String id, String title, String total_amt, String transaction_type, String date, String status, String type) {
        this.id = id;
        this.title = title;
        this.total_amt = total_amt;
        this.transaction_type = transaction_type;
        this.date = date;
        Status = status;
        this.type = type;
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

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
