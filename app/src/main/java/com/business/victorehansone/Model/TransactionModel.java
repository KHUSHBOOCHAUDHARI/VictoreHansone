package com.business.victorehansone.Model;

public class TransactionModel {
    private String id;
    private String title;
    private String total_amt;
    private String payment_method;
    private String transaction_type;
    private String date;
    private String Status;
    private String code;


    public TransactionModel(String id, String title, String total_amt, String payment_method, String transaction_type, String date, String status, String code) {
        this.id = id;
        this.title = title;
        this.total_amt = total_amt;
        this.payment_method = payment_method;
        this.transaction_type = transaction_type;
        this.date = date;
        Status = status;
        this.code = code;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
