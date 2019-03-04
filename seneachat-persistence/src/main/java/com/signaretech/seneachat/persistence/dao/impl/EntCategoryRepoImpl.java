package com.signaretech.seneachat.persistence.dao.impl;

import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class EntCategoryRepoImpl implements EntCategoryRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(EntCategory category) {
        entityManager.persist(category);
    }

    @Override
    public List<EntCategory> findAll() {
        Query query = entityManager.createQuery("select c from EntCategory c");
        return query.getResultList();
    }

    @Override
    public EntCategory findById(UUID id) {
        return entityManager.find(EntCategory.class, id);
    }

    @Override
    public List<EntCategory> findByParentName(String name) {
        Query query = entityManager.createQuery("select c from EntCategory c where c.parent.id = :parent");
        query.setParameter("parent", name);
        return query.getResultList();
    }

    @Override
    public List<EntCategory> findByParentNull() {
        Query query = entityManager.createQuery("select c from EntCategory c where c.parent is null");
        return query.getResultList();
    }

    @Override
    public EntCategory findByName(String name) {

        Query query = entityManager.createQuery("select c from EntCategory c where c.name = :name");
        query.setParameter("name", name);
        return (EntCategory) query.getSingleResult();
    }
}

