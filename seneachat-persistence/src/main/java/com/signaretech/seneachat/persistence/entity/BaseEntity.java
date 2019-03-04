package com.signaretech.seneachat.persistence.entity;

import com.signaretech.seneachat.persistence.utils.UUIDUtil;

import java.util.Arrays;

/**
 * @author Mouhamadou Bop
 *
 * Base class to be extended by all the persistent objects to
 * inherit an id primary key as well as equals and hashCode
 * implementations.
 */
public abstract class BaseEntity implements IPersistentEntity{
    protected byte[] id = UUIDUtil.createUUID();
    protected String uuidString;

    public BaseEntity(){
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public String getUuidString() {
        return UUIDUtil.getUUIDFromByteArray(id).toString();
    }

    public void setUuidString(String uuidString) {
        this.uuidString = uuidString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(id);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseEntity other = (BaseEntity) obj;

        if (!this.id.equals(other.getId()))
            return false;
        return true;
    }
}
