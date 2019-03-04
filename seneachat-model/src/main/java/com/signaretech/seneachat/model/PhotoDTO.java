package com.signaretech.seneachat.model;

import java.util.UUID;

public class PhotoDTO {

    private UUID id;
    private String name;
    private byte[] imageBytes;
    private Boolean primaryInd;
    private String uuidString;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUuidString() {
        return id != null ? id.toString() : null;
    }

    public void setUuidString(String uuidString) {
        this.uuidString = uuidString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Boolean getPrimaryInd() {
        return primaryInd;
    }

    public void setPrimaryInd(Boolean primaryInd) {
        this.primaryInd = primaryInd;
    }
}
