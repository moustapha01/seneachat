package com.signaretech.seneachat.util;

import com.signaretech.seneachat.model.AdvertisementDTO;

public class AdvertisementValidation {
    public static boolean validateAdvertisement(AdvertisementDTO ad) {

        if(ad.getAdLocation().isEmpty() ||
                ad.getDescription().isEmpty() ||
                ad.getPrice() == null ||
                ad.getTitle().isEmpty()){

            return false;
        }

        return true;
    }
}
