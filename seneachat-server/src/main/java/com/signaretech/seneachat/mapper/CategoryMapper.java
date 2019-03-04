package com.signaretech.seneachat.mapper;

import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.model.CategoryDTO;
import org.modelmapper.ModelMapper;

public class CategoryMapper {

    private static ModelMapper mapper = new ModelMapper();

    public static CategoryDTO convertToDto(EntCategory category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setId(category.getId());

        if(category.getParent() != null && category.getParent().getParent() != null){
            categoryDTO.setCategoryLevel3(category.getName());
            categoryDTO.setCategoryLevel2(category.getParent().getName());
            categoryDTO.setCategoryLevel1(category.getParent().getParent().getName());
        }
        else if(category.getParent() != null && category.getParent().getParent() == null){
            categoryDTO.setCategoryLevel2(category.getName());
            categoryDTO.setCategoryLevel1(category.getParent().getName());
        }
        else if(category.getParent() == null){
            categoryDTO.setCategoryLevel1(category.getName());
        }
        return categoryDTO;
    }

    public EntCategory convertToEntity(CategoryDTO categoryDTO) {
        EntCategory category = mapper.map(categoryDTO, EntCategory.class);
        return category;
    }
}
