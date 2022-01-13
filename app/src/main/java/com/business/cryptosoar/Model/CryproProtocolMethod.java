package com.business.cryptosoar.Model;

import java.util.ArrayList;

public class CryproProtocolMethod {
    private String id;
    private String name;
    private String TypeId;
    private String ProId;
    private String address;
    private String label;
    private String Toaddress;
    private String admin_crypto_note;


    public CryproProtocolMethod() {
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

    public String getTypeId() {
        return TypeId;
    }

    public void setTypeId(String typeId) {
        TypeId = typeId;
    }

    public String getProId() {
        return ProId;
    }

    public void setProId(String proId) {
        ProId = proId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getToaddress() {
        return Toaddress;
    }

    public void setToaddress(String toaddress) {
        Toaddress = toaddress;
    }

    public String getAdmin_crypto_note() {
        return admin_crypto_note;
    }

    public void setAdmin_crypto_note(String admin_crypto_note) {
        this.admin_crypto_note = admin_crypto_note;
    }
}
