package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.mapper.AdMapper;
import com.signaretech.seneachat.mapper.CategoryMapper;
import com.signaretech.seneachat.model.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private EntCategoryRepo categoryRepo;
    private final TransactionTemplate transactionTemplate;

    private AdMapper adMapper = new AdMapper();
    private CategoryMapper catMapper = new CategoryMapper();

    private List<CategoryDTO> allCategories;

    private Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    public CategoryServiceImpl(EntCategoryRepo categoryRepo, TransactionTemplate transactionTemplate) {
        this.categoryRepo = categoryRepo;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public List<CategoryDTO> getCategoriesByParent(String name) {

        List<CategoryDTO> childCategories = allCategories.stream().filter( cat ->
                (cat.getCategoryLevel1() != null && cat.getCategoryLevel1().equals(name) && cat.getCategoryLevel2() != null && cat.getCategoryLevel2().equals(cat.getName()) ||
                        (cat.getCategoryLevel2() != null && cat.getCategoryLevel2().equals(name) && cat.getCategoryLevel3() != null && cat.getCategoryLevel3().equals(cat.getName()))
                )).collect(Collectors.toList());

        childCategories.sort(Comparator.comparing( cats -> cats.getName()));
        return childCategories;
    }

    @Override
    public List<CategoryDTO> getRootCategories() {
        if(allCategories == null){
            getAllCategories();
        }

        List<CategoryDTO> rootCategories = allCategories.stream().filter( cat -> cat.getCategoryLevel2() == null).collect(Collectors.toList());
        rootCategories.sort(Comparator.comparing( cats -> cats.getName()));
        return rootCategories;
    }

    @Override
    public CategoryDTO getCatgeoryByName(String name) {
        if(allCategories == null){
            getAllCategories();
        }

        return allCategories.stream().filter( cat -> cat.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {

        if(allCategories == null){
            LOG.info("START retrieving all advertisement categories");
            List<EntCategory> categories = transactionTemplate.execute(new TransactionCallback<List<EntCategory>>() {
                @Override
                public List<EntCategory> doInTransaction(TransactionStatus transactionStatus) {
                    return categoryRepo.findAll();
                }
            });

            allCategories = categories.stream().map( cat -> CategoryMapper.convertToDto(cat)).collect(Collectors.toList());
        }
        return allCategories;
    }



    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        EntCategory category = catMapper.convertToEntity(categoryDTO);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                categoryRepo.create(category);
            }
        });

        return categoryDTO;
    }


}

