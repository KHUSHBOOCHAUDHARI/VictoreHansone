package com.business.cryptosoar.Model;

import java.util.ArrayList;

public class PaymentOptModel {
    private String  id;
    private String name;
    private String bankaccooutno;
    private String bankaccountname;
    private String bankaccountifsc;
    private String contactname;
    private String contactno;
    private String state;
    ArrayList arrayList = new ArrayList();

    public PaymentOptModel(String id, String name, String bankaccooutno, String bankaccountname, String bankaccountifsc, String contactname, String contactno, String state, ArrayList arrayList) {
        this.id = id;
        this.name = name;
        this.bankaccooutno = bankaccooutno;
        this.bankaccountname = bankaccountname;
        this.bankaccountifsc = bankaccountifsc;
        this.contactname = contactname;
        this.contactno = contactno;
        this.state = state;
        this.arrayList = arrayList;
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

    public String getBankaccooutno() {
        return bankaccooutno;
    }

    public void setBankaccooutno(String bankaccooutno) {
        this.bankaccooutno = bankaccooutno;
    }

    public String getBankaccountname() {
        return bankaccountname;
    }

    public void setBankaccountname(String bankaccountname) {
        this.bankaccountname = bankaccountname;
    }

    public String getBankaccountifsc() {
        return bankaccountifsc;
    }

    public void setBankaccountifsc(String bankaccountifsc) {
        this.bankaccountifsc = bankaccountifsc;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }
}
