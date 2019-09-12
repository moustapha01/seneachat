package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.entity.EntPhoto;

import java.util.UUID;

public interface IPhotoService {

    EntPhoto findPhotoById(UUID id);
}
