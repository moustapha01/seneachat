package com.signaretech.seneachat.persistence.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    private static final Logger LOG = LoggerFactory.getLogger(AuditListener.class);

    @PrePersist
    private void prePersist(AuditableEntity auditableEntity) throws InterruptedException {
        LOG.info("The following entity is about to be peristed: {}", auditableEntity.toString());
        Thread.sleep(1000);
        auditableEntity.setCreatedBy("SYSTEM");
        auditableEntity.setLastModifiedBy("SYSTEM");
        auditableEntity.setCreatedDate(LocalDateTime.now());
        auditableEntity.setLastModifiedDate(LocalDateTime.now());
    }

    @PreUpdate
    private void preUpdate(AuditableEntity auditableEntity) throws InterruptedException {
        LOG.info("The following entity is about to be updated: {}", auditableEntity.toString());
        Thread.sleep(1000);
        auditableEntity.setCreatedBy("SYSTEM");
        auditableEntity.setLastModifiedBy("SYSTEM");
        auditableEntity.setCreatedDate(LocalDateTime.now());
        auditableEntity.setLastModifiedDate(LocalDateTime.now());
    }

    @PreRemove
    private void preRemove(AuditableEntity auditableEntity){
        LOG.info("The following entity is about to be delete: {}", auditableEntity.toString());
    }
}
