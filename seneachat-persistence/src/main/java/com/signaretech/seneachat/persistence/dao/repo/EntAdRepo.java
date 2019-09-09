package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntAdRepo extends CrudRepository<EntAdvertisement, UUID> {

    List<EntAdvertisement> findByCategoryId(UUID categoryId);

    List<EntAdvertisement> findBySellerId(UUID id);

   // List<EntAdvertisement> findBySellerIdByPage(UUID id, int from, int max);
}
