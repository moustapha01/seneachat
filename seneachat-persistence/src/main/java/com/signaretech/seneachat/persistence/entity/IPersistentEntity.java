package com.signaretech.seneachat.persistence.entity;

public interface IPersistentEntity {

    public void setId(byte[] id);

    public byte[] getId();
}
