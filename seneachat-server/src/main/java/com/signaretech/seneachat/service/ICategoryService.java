package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.entity.EntCategory;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {

    List<EntCategory> getCategoriesByParent(String name);

    List<EntCategory> getAllCategories();

    List<EntCategory> getRootCategories();

    EntCategory getCatgeoryByName(String name);

    EntCategory createCategory(EntCategory category);
}

