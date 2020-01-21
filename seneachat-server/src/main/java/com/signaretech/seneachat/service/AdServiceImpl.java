package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.PriceFilterEntry;
import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntPhotoRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.model.AdvertisementStatus;
import com.signaretech.seneachat.persistence.entity.EntPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AdServiceImpl implements IAdService {

    private final EntAdRepo adRepo;
    private final EntSellerRepo sellerRepo;
    private final ICategoryService categoryService;
    private final EntCategoryRepo categoryRepo;
    private final EntPhotoRepo photoRepo;

    private static final Logger LOG = LoggerFactory.getLogger(AdServiceImpl.class);

    public AdServiceImpl(EntAdRepo adRepo,
                         EntSellerRepo sellerRepo,
                         ICategoryService categoryService,
                         EntCategoryRepo categoryRepo,
                         EntPhotoRepo photoRepo){
        this.adRepo = adRepo;
        this.sellerRepo = sellerRepo;
        this.categoryService = categoryService;
        this.categoryRepo = categoryRepo;
        this.photoRepo = photoRepo;
    }

    @Override
    public EntAdvertisement createAd(EntAdvertisement ad) {
        ad.setStatus(AdvertisementStatus.PENDING.getValue());
        return adRepo.save(ad);
    }


    @Override
    public EntAdvertisement updateAd(EntAdvertisement ad) {
        LOG.info("Updating advertisement with id {} and number of photos {}", ad.getId(), ad.getPhotos().size());
        if(StringUtils.isEmpty(ad.getStatus())) {
            ad.setStatus(AdvertisementStatus.PENDING.getValue());
        }
        return adRepo.save(ad);
    }


    @Override
    public EntAdvertisement fetchAd(UUID id) {
        LOG.info("Fetching advertisement with id {}", id.toString());
        return adRepo.findById(id).orElse(null);
    }

    @Override
    public EntAdvertisement fetchAdDetail(UUID id) {
        return adRepo.fetchAdDetail(id);
    }


    @Override
    public void deleteAd(EntAdvertisement ad) {
        adRepo.delete(ad);
    }

    @Override
    public void deletePhoto(UUID photoId) {
        photoRepo.deleteById(photoId);
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

        Double minRange = Math.pow(10, (int) (Math.log10(min) + 1));
        Double maxRange = Math.pow(10, (int) (Math.log10(max)));

        //Divide the price range into 10 steps.
        Long step = (maxRange.longValue() - minRange.longValue()) / 10;

        Double increment =  Math.pow(10, (int) (Math.log10(step) + 1));

        List<PriceFilterEntry> priceFilters = new ArrayList<>();
        priceFilters.add(new PriceFilterEntry(String.valueOf(min.longValue()), min.longValue(), increment.longValue()));
        for(Double from = minRange; from < maxRange; from += increment) {
            PriceFilterEntry entry = new PriceFilterEntry(from.toString(), from.longValue(), from.longValue() + increment.longValue());
            priceFilters.add(entry);
        }

        return priceFilters;
    }
}

