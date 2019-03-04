package com.signaretech.seneachat.persistence.dao.impl;

import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.UUID;

@Repository
public class EntSellerRepoImpl implements EntSellerRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntSeller findEntSellerByEmail(String email) {
        entityManager.clear();
        Query query = entityManager.createQuery("select s from EntSeller s where s.email = :email");
        query.setParameter("email", email);
        return (EntSeller) query.getSingleResult();
    }

    @Override
    public void create(EntSeller seller) {
        entityManager.persist(seller);
    }


    @Override
    public void update(EntSeller seller) {
        entityManager.merge(seller);
    }

    @Override
    public EntSeller findBySellerId(UUID id) {
        return entityManager.find(EntSeller.class, id);
    }
}
