package com.signaretech.seneachat.service;

import com.google.common.collect.Lists;
import com.signaretech.seneachat.model.*;
import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntPhotoRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntUserRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AdServiceImpl implements IAdService {

    private final EntAdRepo adRepo;
    private final EntUserRepo sellerRepo;
    private final ICategoryService categoryService;
    private final EntCategoryRepo categoryRepo;
    private final EntPhotoRepo photoRepo;

    private static final Logger LOG = LoggerFactory.getLogger(AdServiceImpl.class);

    public AdServiceImpl(EntAdRepo adRepo,
                         EntUserRepo sellerRepo,
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
    @Transactional( readOnly = true )
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
    public List<EntAdvertisement> getSellerAds(UUID userId, int fromPage, int maxAds) {
                return adRepo.findByUserId(userId);
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
    public List<EntAdvertisement> findByParentCategoryAndFilter(String categoryName, AdvertisementFilter adFilter) {
        final EntCategory category = categoryService.getCategoryByName(categoryName);

        if(!adFilter.getSelectedCategories().isEmpty()) {
            return findBySubCategoriesAndFilter(adFilter);
        }

        List<ItemCondition> selectedItemConditions = getSelectedItemConditions(adFilter);

        return adRepo.findByParentCategoryAndFilter(category.getId(),
                adFilter.getSelectedPrice().doubleValue(),
                selectedItemConditions);
    }

    @Override
    public List<EntAdvertisement> findBySubCategoriesAndFilter(AdvertisementFilter adFilter) {

        List<EntCategory> allCategories = StreamSupport
                .stream(categoryService.getAllCategories().spliterator(), false)
                .collect(Collectors.toList());

        List<UUID> selectedCategoryIds = allCategories.stream()
                .filter( cat -> adFilter.getSelectedCategories().contains(cat.getName()))
                .map(EntCategory::getId)
                .collect(Collectors.toList());

        List<ItemCondition> selectedItemConditions = getSelectedItemConditions(adFilter);

        List<EntAdvertisement> ads = adRepo.findBySubCategoryAndFilter(selectedCategoryIds,
                adFilter.getSelectedPrice().doubleValue(),
                selectedItemConditions);

        return ads;
    }


    @Override
    public PriceRange getPriceRange(List<EntAdvertisement> ads) {
        Double min = ads.stream().mapToDouble( v -> v.getPrice() ).min().orElseThrow(NoSuchElementException::new);
        Double max = ads.stream().mapToDouble( v -> v.getPrice() ).max().orElseThrow(NoSuchElementException::new);
        return new PriceRange(Math.round(min), Math.round(max));
    }

    private List<ItemCondition> getSelectedItemConditions(AdvertisementFilter adFilter) {
        return adFilter.getSelectedConditions().isEmpty() ?
                Arrays.stream(ItemCondition.values()).collect(Collectors.toList()) :
                adFilter.getSelectedConditions();
    }
}

