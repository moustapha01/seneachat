package com.signaretech.seneachat.model;

public enum SellerStatus {
    PENDING("P"),
    ACTIVE("A"),
    CANCEL("C"),
    HISTORY("H");

    private String value;


    SellerStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
