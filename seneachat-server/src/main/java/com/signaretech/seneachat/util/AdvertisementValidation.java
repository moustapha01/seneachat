package com.signaretech.seneachat.util;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;;

public class AdvertisementValidation {
    public static boolean validateAdvertisement(EntAdvertisement ad) {

        if(ad.getAdLocation().isEmpty() ||
                ad.getDescription().isEmpty() ||
                ad.getPrice() == null ||
                ad.getTitle().isEmpty()){

            return false;
        }

        return true;
    }
}
