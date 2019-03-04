package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {

    List<CategoryDTO> getCategoriesByParent(String name);

    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> getRootCategories();

    CategoryDTO getCatgeoryByName(String name);

    CategoryDTO createCategory(CategoryDTO categoryDTO);
}

