package com.signaretech.seneachat.persistence.entity;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "ad_photos")
public class EntPhoto extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "photo_uuid", nullable = false, updatable = false)
    private UUID id;

    @Basic
    @Column( name = "photo_name", nullable = false)
    private String name;

    @Lob
    @Column(name = "image_byte", nullable = false)
    private byte[] imageBytes;

    @Basic(optional = true)
    @Column(name = "primary_ind")
    private Boolean primaryInd;

    @ManyToOne
    @JoinColumn(name = "advertisement_uuid", referencedColumnName = "advertisement_uuid", nullable = false)
    private EntAdvertisement advertisement;

    public EntPhoto(){
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public EntAdvertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(EntAdvertisement advertisement) {
        this.advertisement = advertisement;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }
}

