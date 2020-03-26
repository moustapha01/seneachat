package com.signaretech.seneachat.persistence.entity;

import org.springframework.data.annotation.Version;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AuditableEntity {

//    @Version
//    private Long version = 0l;

    @Basic
    @Column(name = "created_date", nullable = false)
    protected LocalDateTime createdDate;

    @Basic
    @Column(name = "last_modified_date", nullable = false)
    protected LocalDateTime lastModifiedDate;

    @Basic
    @Column(name = "created_by", nullable = false)
    protected String createdBy;

    @Basic
    @Column(name = "last_modified_by", nullable = false)
    protected String lastModifiedBy;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

//    public Long getVersion() {
//        return version;
//    }
//
//    public void setVersion(Long version) {
//        this.version = version;
//    }
}
