package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.entity.EntCategory;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {

    List<EntCategory> getCategoriesByParent(String name);

    Iterable<EntCategory> getAllCategories();

    List<EntCategory> getRootCategories();

    EntCategory getCategoryByName(String name);

    EntCategory createCategory(EntCategory category);

}

