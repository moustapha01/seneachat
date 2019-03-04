package com.signaretech.seneachat.model;

public enum AdvertisementStatus {
    PENDING("P"),
    ACTIVE("A"),
    CANCEL("C"),
    HISTORY("H");

    private String value;


    AdvertisementStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
