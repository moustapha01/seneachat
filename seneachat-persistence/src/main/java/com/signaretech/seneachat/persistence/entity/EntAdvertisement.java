package com.signaretech.seneachat.persistence.entity;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "advertisements")
public class EntAdvertisement extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "advertisement_id", nullable = false, updatable = false)
    private UUID id;

    @Basic
    @Column(name = "title", nullable = false)
    @NotBlank(message = "{title.notblank}")
    private String title;

    @Basic
    @Column(name = "description", nullable = false)
    @NotBlank(message = "{description.notblank}")
    private String description;

    @Basic
    @Column(name = "brand", nullable = true)
    private String brand;

    @Basic
    @Column(name = "color", nullable = true)
    private String color;

    @Basic
    @Column(name = "summary", nullable = true)
    private String summary;

    @Basic
    @Column(name = "price", nullable = false)
    @NotNull(message = "{price.notblank}")
    @Min(message = "{price.min}", value = 1)
    @Max(message = "{price.max}", value = Long.MAX_VALUE)
    private Double price;

    @Basic
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @Basic
    @Column( name = "num_views")
    private Integer numViews;

    @Basic
    @Column ( name = "ad_location")
    @NotBlank(message = "{location.notblank}")
    private String adLocation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "category_uuid", referencedColumnName = "category_uuid", nullable = false)
    @NotNull(message = "{category.notnull}")
    private EntCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private EntUser user;

    @OneToMany( mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<EntPhoto> photos = new ArrayList<>();

    public EntAdvertisement() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public EntCategory getCategory() {
        return category;
    }

    public void setCategory(EntCategory category) {
        this.category = category;
    }

    public EntUser getUser() {
        return user;
    }

    public void setUser(EntUser seller) {
        this.user = seller;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNumViews() {
        return numViews;
    }

    public void setNumViews(Integer numViews) {
        this.numViews = numViews;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(String adLocation) {
        this.adLocation = adLocation;
    }

    public List<EntPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<EntPhoto> photos) {
        this.photos = photos;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("status", status)
                .add("price", price)
                .toString();
    }
}

