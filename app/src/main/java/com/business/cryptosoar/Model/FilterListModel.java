package com.business.cryptosoar.Model;

public class FilterListModel {
    private String id;
    private String title;
    private String total_amt;
    private String fund_plan_type;
    private String date;
    private String Status;


    public FilterListModel(String id, String title, String total_amt, String fund_plan_type, String date, String status) {
        this.id = id;
        this.title = title;
        this.total_amt = total_amt;
        this.fund_plan_type = fund_plan_type;
        this.date = date;
        Status = status;
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

    public String getFund_plan_type() {
        return fund_plan_type;
    }

    public void setFund_plan_type(String fund_plan_type) {
        this.fund_plan_type = fund_plan_type;
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
}
