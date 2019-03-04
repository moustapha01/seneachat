package com.signaretech.seneachat.mapper;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntPhoto;
import com.signaretech.seneachat.model.AdvertisementDTO;
import com.signaretech.seneachat.model.PhotoDTO;

public class AdMapper {

    public static AdvertisementDTO convertToDto(EntAdvertisement ad){
        AdvertisementDTO adDto = new AdvertisementDTO();
        adDto.setId(ad.getId());
        adDto.setUuidString( ad.getId() != null ? ad.getId().toString() : null);
        adDto.setTitle(ad.getTitle());
        adDto.setDescription(ad.getDescription());
        adDto.setAdLocation(ad.getAdLocation());
        adDto.setNumViews(ad.getNumViews());
        adDto.setPrice(ad.getPrice());
        adDto.setStatus(ad.getStatus());

        adDto.setCreatedBy(ad.getCreatedBy());
        adDto.setLastMofifiedBy(ad.getLastModifiedBy());
        adDto.setCreatedDate(ad.getCreatedDate());
        adDto.setLastModifiedDate(ad.getLastModifiedDate());

        adDto.setCategory(CategoryMapper.convertToDto(ad.getCategory()));

//        adDto.setSeller(SellerMapper.convertToDto(ad.getSeller()));

        for(EntPhoto adPhoto: ad.getPhotos()){
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setImageBytes(adPhoto.getImageBytes());
            photoDTO.setName(adPhoto.getName());
            photoDTO.setId(adPhoto.getId());
            photoDTO.setPrimaryInd(adPhoto.getPrimaryInd());
            photoDTO.setUuidString(adPhoto.getId().toString());
            adDto.getPhotos().add(photoDTO);
        }

        return adDto;
    }

    public static EntAdvertisement convertToEntity(AdvertisementDTO ad){

        EntAdvertisement entAd = new EntAdvertisement();
        entAd.setId(ad.getId());
        entAd.setAdLocation(ad.getAdLocation());
        entAd.setDescription(ad.getDescription());
        entAd.setNumViews(ad.getNumViews());
        entAd.setPrice(ad.getPrice());
        entAd.setStatus(ad.getStatus());
        entAd.setTitle(ad.getTitle());

        for(PhotoDTO photoDTO: ad.getPhotos()){
            EntPhoto entPhoto = new EntPhoto();
            entPhoto.setAdvertisement(entAd);
            entPhoto.setId(photoDTO.getId());
            entPhoto.setImageBytes(photoDTO.getImageBytes());
            entPhoto.setName(photoDTO.getName());
            entPhoto.setPrimaryInd(photoDTO.getPrimaryInd());
            entAd.getPhotos().add(entPhoto);
        }

//        EntSeller entSeller = new EntSeller();
//        entSeller.setId(ad.getSeller().getId());
//        entSeller.setStatus(ad.getSeller().getStatus());
//        entSeller.setFirstName(ad.getSeller().getFirstName());
//        entSeller.setLastName(ad.getSeller().getLastName());
//        entSeller.setActivationCode(ad.getSeller().getActivationCode());
//        entSeller.setCellPhone(ad.getSeller().getCellPhone());
//        entSeller.setEmail(ad.getSeller().getEmail());
//        entSeller.setHomePhone(ad.getSeller().getHomePhone());
//        entSeller.setSecret(ad.getSeller().getSecret());

        entAd.setSeller(SellerMapper.convertToEntity(ad.getSeller()));

        return entAd;
    }
}

