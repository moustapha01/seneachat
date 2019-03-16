package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.mapper.CategoryMapper;
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

    private CategoryMapper catMapper = new CategoryMapper();

    private List<EntCategory> allCategories;

    private Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    public CategoryServiceImpl(EntCategoryRepo categoryRepo, TransactionTemplate transactionTemplate) {
        this.categoryRepo = categoryRepo;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public List<EntCategory> getCategoriesByParent(String name) {

        List<EntCategory> childCategories = allCategories.stream().filter( cat ->
                (cat.getParent() != null && cat.getParent().equals(name) && cat.getParent().getParent() != null && cat.getParent().getParent().equals(cat.getName()) ||
                        (cat.getParent().getParent() != null && cat.getParent().getParent().equals(name) && cat.getParent().getParent().getParent() != null && cat.getParent().getParent().getParent().equals(cat.getName()))
                )).collect(Collectors.toList());

        childCategories.sort(Comparator.comparing( cats -> cats.getName()));
        return childCategories;
    }

    @Override
    public List<EntCategory> getRootCategories() {
        if(allCategories == null){
            getAllCategories();
        }

        List<EntCategory> rootCategories = allCategories.stream().filter( cat -> cat.getParent() == null).collect(Collectors.toList());
        rootCategories.sort(Comparator.comparing( cats -> cats.getName()));
        return rootCategories;
    }

    @Override
    public EntCategory getCatgeoryByName(String name) {
        if(allCategories == null){
            getAllCategories();
        }

        return allCategories.stream().filter( cat -> cat.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public List<EntCategory> getAllCategories() {

        if(allCategories == null){
            LOG.info("START retrieving all advertisement categories");
            List<EntCategory> categories = transactionTemplate.execute(new TransactionCallback<List<EntCategory>>() {
                @Override
                public List<EntCategory> doInTransaction(TransactionStatus transactionStatus) {
                    return categoryRepo.findAll();
                }
            });

            return categories;
        }
        return allCategories;
    }

    @Override
    public EntCategory createCategory(EntCategory category) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                categoryRepo.create(category);
            }
        });

        return category;
    }
}

