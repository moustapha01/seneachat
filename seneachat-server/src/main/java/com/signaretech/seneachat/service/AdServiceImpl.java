package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.PriceFilterEntry;
import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.model.AdvertisementStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class AdServiceImpl implements IAdService {

    private EntAdRepo adRepo;
    private EntSellerRepo sellerRepo;
    private ICategoryService categoryService;
    private EntCategoryRepo categoryRepo;

    private static final Logger LOG = LoggerFactory.getLogger(AdServiceImpl.class);

    @Autowired
    public AdServiceImpl(EntAdRepo adRepo, EntSellerRepo sellerRepo, ICategoryService categoryService,
                         EntCategoryRepo categoryRepo){
        this.adRepo = adRepo;
        this.sellerRepo = sellerRepo;
        this.categoryService = categoryService;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public EntAdvertisement createAd(EntAdvertisement ad) {
        ad.setStatus(AdvertisementStatus.PENDING.getValue());
        return adRepo.save(ad);
    }


    @Override
    public EntAdvertisement updateAd(EntAdvertisement ad) {
        LOG.info("Updating advertisement with id {} and number of photos {}", ad.getId(), ad.getPhotos().size());
        return adRepo.save(ad);
    }


    @Override
    public EntAdvertisement fetchAd(UUID id) {
        LOG.info("Fetching advertisement with id {}", id.toString());
        return adRepo.findById(id).orElse(null);
    }


    @Override
    public void deleteAd(EntAdvertisement ad) {
        adRepo.delete(ad);
    }


    @Override
    public List<EntAdvertisement> getSellerAds(UUID sellerId, int fromPage, int maxAds) {
                return adRepo.findBySellerId(sellerId);
    }




    @Override
    public EntAdvertisement approveAd(EntAdvertisement ad) {
        ad.setStatus(AdvertisementStatus.ACTIVE.name());
        return updateAd(ad);
    }

    @Override
    public List<EntAdvertisement> getCategoryAds(String categoryName) {
        final EntCategory category = categoryService.getCategoryByName(categoryName);
        return adRepo.findByCategory(category.getId());
    }

    @Override
    public List<PriceFilterEntry> getPriceFilters(List<EntAdvertisement> ads) {
        Double min = ads.stream().mapToDouble( v -> v.getPrice() ).min().orElseThrow(NoSuchElementException::new);
        Double max = ads.stream().mapToDouble( v -> v.getPrice() ).max().orElseThrow(NoSuchElementException::new);

        //Divide the price range into 10 steps.
        Long step = (max.longValue() - min.longValue()) / 10;

        //Use the number of digits for the step as the multiplier that will be used to calculate the price range
        //ex: [multiplier = 2, range increment = 100]; [multiplier = 3, range increment = 1000]
        int multiplier = step.toString().length();
        Double increment = Math.pow(10, multiplier);

        return IntStream.rangeClosed(1, 10).mapToObj( i -> {
            Long minValue = i * increment.longValue();
            return new PriceFilterEntry(minValue.toString(), minValue, minValue + increment.longValue());
        }).collect(Collectors.toList());
    }
}

