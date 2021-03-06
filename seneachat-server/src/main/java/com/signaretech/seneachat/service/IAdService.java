package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.AdvertisementFilter;
import com.signaretech.seneachat.model.PriceFilterEntry;
import com.signaretech.seneachat.model.PriceRange;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.persistence.entity.EntPhoto;

import java.util.List;
import java.util.UUID;

public interface IAdService {

    EntAdvertisement createAd(EntAdvertisement ad);

    EntAdvertisement updateAd(EntAdvertisement ad);

    EntAdvertisement fetchAd(UUID id);

    EntAdvertisement fetchAdDetail(UUID id);

    void deleteAd(EntAdvertisement ad);

    void deletePhoto(UUID photo);

    List<EntAdvertisement> getSellerAds(UUID sellerId, int fromPage, int maxAds);

    EntAdvertisement approveAd(EntAdvertisement ad);

    List<EntAdvertisement> getCategoryAds(String categoryName);

    List<EntAdvertisement> findByParentCategoryAndFilter(String categoryName, AdvertisementFilter adFilter);

    List<EntAdvertisement> findBySubCategoriesAndFilter(AdvertisementFilter adFilter);

    PriceRange getPriceRange(List<EntAdvertisement> ads);
}
