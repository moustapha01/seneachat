package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;

import java.util.List;
import java.util.UUID;

public interface EntAdRepo {

    EntAdvertisement create(EntAdvertisement ad);

    EntAdvertisement update(EntAdvertisement ad);

    void delete(EntAdvertisement ad);

    EntAdvertisement findById(UUID id);

    List<EntAdvertisement> findByCategoryId(UUID categoryId);

    List<EntAdvertisement> findBySellerId(UUID id);

    List<EntAdvertisement> findBySellerIdByPage(UUID id, int from, int max);
}
