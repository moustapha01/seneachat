package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.dao.repo.EntPhotoRepo;
import com.signaretech.seneachat.persistence.entity.EntPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhotoService implements IPhotoService {

    private EntPhotoRepo photoRepo;

    @Autowired
    public PhotoService(EntPhotoRepo photoRepo) {
        this.photoRepo = photoRepo;
    }

    @Override
    public EntPhoto findPhotoById(UUID id) {
        return photoRepo.findById(id).orElse(null);
    }
}
