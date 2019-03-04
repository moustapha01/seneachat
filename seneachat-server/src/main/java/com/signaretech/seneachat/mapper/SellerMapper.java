package com.signaretech.seneachat.mapper;

import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.model.SellerDTO;

import java.util.stream.Collectors;

public class SellerMapper {

    public static SellerDTO convertToDTO(EntSeller seller) {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setEmail(seller.getEmail());
        sellerDTO.setStatus(seller.getStatus());
        sellerDTO.setSecret(seller.getSecret());
        sellerDTO.setActivationCode(seller.getActivationCode());
        sellerDTO.setHomePhone(seller.getHomePhone());
        sellerDTO.setCellPhone(seller.getCellPhone());
        sellerDTO.setLastName(seller.getLastName());
        sellerDTO.setFirstName(seller.getFirstName());
        sellerDTO.setId(seller.getId());

        sellerDTO.setCreatedDate(seller.getCreatedDate());
        sellerDTO.setLastModifiedDate(seller.getLastModifiedDate());
        sellerDTO.setCreatedBy(seller.getCreatedBy());
        sellerDTO.setLastModifiedBy(seller.getLastModifiedBy());

//        sellerDTO.setAds( seller.getAds() != null ? seller.getAds().stream().map( ad -> convertAd(ad)).collect(Collectors.toList()) : null);

        return sellerDTO;
    }

    public static EntSeller convertToEntity(SellerDTO sellerDTO) {
        EntSeller seller = new EntSeller();
        seller.setEmail(sellerDTO.getEmail());
        seller.setStatus(sellerDTO.getStatus());
        seller.setSecret(sellerDTO.getSecret());
        seller.setActivationCode(sellerDTO.getActivationCode());
        seller.setHomePhone(sellerDTO.getHomePhone());
        seller.setCellPhone(sellerDTO.getCellPhone());
        seller.setLastName(sellerDTO.getLastName());
        seller.setFirstName(sellerDTO.getFirstName());
        seller.setId(sellerDTO.getId());

        seller.setCreatedBy(sellerDTO.getCreatedBy());
        seller.setCreatedDate(sellerDTO.getCreatedDate());
        seller.setLastModifiedBy(sellerDTO.getLastModifiedBy());
        seller.setLastModifiedDate(sellerDTO.getLastModifiedDate());

        seller.setAds( sellerDTO.getAds() != null ? sellerDTO.getAds().stream().map( ad -> AdMapper.convertToEntity(ad)).collect(Collectors.toList()) : null);

        return seller;
    }
}

