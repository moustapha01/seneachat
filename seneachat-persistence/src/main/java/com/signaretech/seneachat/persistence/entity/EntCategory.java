package com.signaretech.seneachat.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity object that represents a {@link EntAdvertisement} category.
 * In addition to the UUID primary key, it includes a name and a parent
 * property that represents the parent category. Therefore, all the categories
 * are organized in a tree format. At the top of the tree, you have the root
 * categories which have a null parent. All other categories have non null parent.
 */
@Entity
@Table(name = "ad_categories")
public class EntCategory {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "category_uuid", nullable = false, updatable = false)
    private UUID id;

    @Basic
    @Column(name = "category_name", unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "parent", referencedColumnName = "category_uuid")
    private EntCategory parent;

    public EntCategory() {
    }

    public EntCategory(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntCategory)) return false;
        EntCategory category = (EntCategory) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

