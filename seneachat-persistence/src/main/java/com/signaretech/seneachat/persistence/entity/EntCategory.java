package com.signaretech.seneachat.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ad_categories")
public class EntCategory {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "category_uuid", nullable = false, updatable = false)
    private UUID id;

    @Basic
    @Column(name = "category_name")
    private String name;

    @OneToOne
    @JoinColumn(name = "parent", referencedColumnName = "category_uuid")
    private EntCategory parent;

    public EntCategory() {
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

    public EntCategory getParent() {
        return parent;
    }

    public void setParent(EntCategory parent) {
        this.parent = parent;
    }
}

