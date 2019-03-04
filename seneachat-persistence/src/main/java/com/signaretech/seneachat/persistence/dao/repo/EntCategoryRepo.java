package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntCategory;

import java.util.List;
import java.util.UUID;

public interface EntCategoryRepo {

    void create(EntCategory category);

    EntCategory findById(UUID id);

    List<EntCategory> findAll();

    List<EntCategory> findByParentName(String name);

    List<EntCategory> findByParentNull();

    EntCategory findByName(String name);
}
