package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.mapper.CategoryMapper;
import com.signaretech.seneachat.model.AdvertisementStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class AdServiceImpl implements IAdService {

    private EntAdRepo adRepo;
    private EntSellerRepo sellerRepo;
    private ICategoryService categoryService;
    private EntCategoryRepo categoryRepo;

    private static final Logger LOG = LoggerFactory.getLogger(AdServiceImpl.class);

    @Autowired
    public AdServiceImpl(EntAdRepo adRepo, EntSellerRepo sellerRepo, ICategoryService categoryService,
                         EntCategoryRepo categoryRepo, PlatformTransactionManager transactionManager){
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
}

