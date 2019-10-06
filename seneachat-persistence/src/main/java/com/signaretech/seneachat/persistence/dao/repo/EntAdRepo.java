package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntAdRepo extends CrudRepository<EntAdvertisement, UUID> {

    List<EntAdvertisement> findByCategoryId(UUID categoryId);

    @Query("Select ad from EntAdvertisement ad left join fetch ad.seller where ad.category.id=?1 or ad.category.parent.id=?1")
    List<EntAdvertisement> findByCategory(UUID id);

    List<EntAdvertisement> findBySellerId(UUID id);

    @Query("Select ad from EntAdvertisement ad left join fetch ad.seller where ad.id=?1")
    EntAdvertisement fetchAdDetail(UUID id);

   // List<EntAdvertisement> findBySellerIdByPage(UUID id, int from, int max);
}
