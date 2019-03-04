package com.signaretech.seneachat.persistence.dao.impl;

import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class EntAdvertisementRepoImpl implements EntAdRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EntAdvertisement> findBySellerId(UUID id) {
        Query query = entityManager.createQuery("select ad from EntAdvertisement ad where ad.seller.id = :sellerId");
        query.setParameter("sellerId", id);
        return query.getResultList();
    }

    @Override
    public List<EntAdvertisement> findBySellerIdByPage(UUID id, int from, int max) {
        Query query = entityManager.createQuery("select ad from EntAdvertisement ad where ad.seller.id = :sellerId order by ad.createdDate desc");
        query.setFirstResult(from);
        query.setMaxResults(max);
        query.setParameter("sellerId", id);
        return query.getResultList();
    }

    @Override
    public EntAdvertisement create(EntAdvertisement ad) {
        entityManager.persist(ad);
        entityManager.flush();
        return ad;
    }

    @Override
    public EntAdvertisement update(EntAdvertisement ad) {
        return entityManager.merge(ad);
    }

    @Override
    public EntAdvertisement findById(UUID id) {
        return entityManager.find(EntAdvertisement.class, id);
    }

    @Override
    public void delete(EntAdvertisement ad) {
        entityManager.remove(entityManager.contains(ad) ? ad : entityManager.merge(ad));
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public List<EntAdvertisement> findByCategoryId(UUID categoryId) {
        return null;
    }
}
