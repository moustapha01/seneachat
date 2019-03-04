package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.AdvertisementDTO;

import java.util.List;
import java.util.UUID;

public interface IAdService {

    AdvertisementDTO createAd(AdvertisementDTO ad);

    AdvertisementDTO updateAd(AdvertisementDTO ad);

    AdvertisementDTO fetchAd(UUID id);

    void deleteAd(AdvertisementDTO ad);

    List<AdvertisementDTO> getSellerAds(UUID sellerId, int fromPage, int maxAds);

    AdvertisementDTO approveAd(AdvertisementDTO ad);
}
