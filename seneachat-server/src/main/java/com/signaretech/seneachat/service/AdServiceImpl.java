package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.dao.repo.EntAdRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.mapper.AdMapper;
import com.signaretech.seneachat.mapper.CategoryMapper;
import com.signaretech.seneachat.model.AdvertisementDTO;
import com.signaretech.seneachat.model.AdvertisementStatus;
import com.signaretech.seneachat.model.CategoryDTO;
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
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements IAdService {

    private EntAdRepo adRepo;
    private EntSellerRepo sellerRepo;
    private ICategoryService categoryService;
    private EntCategoryRepo categoryRepo;

    private final TransactionTemplate transactionTemplate;

    private AdMapper adMapper = new AdMapper();
    private CategoryMapper categoryMapper = new CategoryMapper();

    private static final Logger LOG = LoggerFactory.getLogger(AdServiceImpl.class);

    @Autowired
    public AdServiceImpl(EntAdRepo adRepo, EntSellerRepo sellerRepo, ICategoryService categoryService,
                         EntCategoryRepo categoryRepo, PlatformTransactionManager transactionManager){
        this.adRepo = adRepo;
        this.sellerRepo = sellerRepo;
        this.categoryService = categoryService;
        this.categoryRepo = categoryRepo;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public AdvertisementDTO createAd(AdvertisementDTO ad) {
        ad.setStatus(AdvertisementStatus.PENDING.getValue());
        EntAdvertisement adEnt = adMapper.convertToEntity(ad);

        EntAdvertisement savedAd = transactionTemplate.execute(new TransactionCallback<EntAdvertisement>() {
            @Override
            public EntAdvertisement doInTransaction(TransactionStatus transactionStatus) {

                EntSeller seller = sellerRepo.findEntSellerByEmail(ad.getSeller().getEmail());
                adEnt.setSeller(seller);

                CategoryDTO catDTO = null;

                if(ad.getCategory().getCategoryLevel3() != null){
                    catDTO = categoryService.getCatgeoryByName(ad.getCategory().getCategoryLevel3());
                }
                else if(ad.getCategory().getCategoryLevel2() != null){
                    catDTO = categoryService.getCatgeoryByName(ad.getCategory().getCategoryLevel2());
                }
                else if(ad.getCategory().getCategoryLevel1() != null){
                    catDTO = categoryService.getCatgeoryByName(ad.getCategory().getCategoryLevel2());
                }

                EntCategory entCategory = categoryRepo.findByName(catDTO.getName());
                adEnt.setCategory(entCategory);
                EntAdvertisement savedAd = adRepo.create(adEnt);
                return savedAd;
            }
        });

        AdvertisementDTO savedAdDto = adMapper.convertToDto(savedAd);
        return savedAdDto;
    }


    @Override
    public AdvertisementDTO updateAd(AdvertisementDTO ad) {
        LOG.info("Updating advertisement with id {} and number of photos {}", ad.getId(), ad.getPhotos().size());
        EntAdvertisement adEnt = adMapper.convertToEntity(ad);

        EntCategory entCategory = null;

        if(ad.getCategory().getCategoryLevel3() != null){
            entCategory = categoryRepo.findByName(ad.getCategory().getCategoryLevel3());
        }
        else if(ad.getCategory().getCategoryLevel2() != null){
            entCategory = categoryRepo.findByName(ad.getCategory().getCategoryLevel2());
        }
        else if(ad.getCategory().getCategoryLevel1() != null){
            entCategory = categoryRepo.findByName(ad.getCategory().getCategoryLevel2());
        }

        adEnt.setCategory(entCategory);

        EntAdvertisement updatedAd  = transactionTemplate.execute(new TransactionCallback<EntAdvertisement>() {

            @Override
            public EntAdvertisement doInTransaction(TransactionStatus transactionStatus) {
                return adRepo.update(adEnt);
            }
        });

        return fetchAd(ad.getId());
    }


    @Override
    public AdvertisementDTO fetchAd(UUID id) {
        LOG.info("Fetching advertisement with id {}", id.toString());
        EntAdvertisement ad = adRepo.findById(id);
        CategoryDTO categoryDTO = categoryMapper.convertToDto(ad.getCategory());
        AdvertisementDTO advertisementDTO = adMapper.convertToDto(ad);
        advertisementDTO.setCategory(categoryDTO);
        return advertisementDTO;
    }


    @Override
    public void deleteAd(AdvertisementDTO ad) {
        EntAdvertisement adEnt = adMapper.convertToEntity(ad);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                adRepo.delete(adEnt);
            }
        });


    }


    @Override
    public List<AdvertisementDTO> getSellerAds(UUID sellerId, int fromPage, int maxAds) {
        List<EntAdvertisement> sellerAds = transactionTemplate.execute(new TransactionCallback<List<EntAdvertisement>>() {
            @Override
            public List<EntAdvertisement> doInTransaction(TransactionStatus transactionStatus) {
                return adRepo.findBySellerIdByPage(sellerId, fromPage, maxAds);
            }
        });
        return sellerAds.stream().map(ad -> AdMapper.convertToDto(ad)).collect(Collectors.toList());
    }




    @Override
    public AdvertisementDTO approveAd(AdvertisementDTO ad) {
        ad.setStatus(AdvertisementStatus.ACTIVE.name());
        return updateAd(ad);
    }
}

