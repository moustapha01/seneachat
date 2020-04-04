package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.model.ItemCondition;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntAdRepo extends CrudRepository<EntAdvertisement, UUID> {

    List<EntAdvertisement> findByCategoryId(UUID categoryId);

    @Query("Select ad from EntAdvertisement ad left join fetch ad.user where ad.category.id=?1 or ad.category.parent.id=?1")
    List<EntAdvertisement> findByCategory(UUID id);

    List<EntAdvertisement> findByUserId(UUID id);

    @Query("Select ad from EntAdvertisement ad left join fetch ad.user where ad.id=?1")
    EntAdvertisement fetchAdDetail(UUID id);

    @Query("select ad from EntAdvertisement ad left join fetch ad.user where (ad.category.id=?1 or ad.category.parent.id=?1) and ad.price<=?2 and ad.condition in ?3")
    List<EntAdvertisement> findByParentCategoryAndFilter(UUID categoryId, Double maxPrice, List<ItemCondition> itemConditions);

    @Query("select ad from EntAdvertisement ad left join fetch ad.user where ad.category.id in ?1 and ad.price<=?2 and ad.condition in ?3")
    List<EntAdvertisement> findBySubCategoryAndFilter(List<UUID> categoryIds, Double maxPrice, List<ItemCondition> itemConditions);


    // List<EntAdvertisement> findBySellerIdByPage(UUID id, int from, int max);
}
