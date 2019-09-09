package com.signaretech.seneachat.service;

import com.google.common.collect.Streams;
import com.signaretech.seneachat.persistence.dao.repo.EntCategoryRepo;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private EntCategoryRepo categoryRepo;

    private Iterable<EntCategory> allCategories;

    private Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    public CategoryServiceImpl(EntCategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<EntCategory> getCategoriesByParent(String name) {

        if(allCategories == null){
            allCategories = getAllCategories();
        }

        return Streams.stream(allCategories)
                .filter( cat -> cat.getParent() != null && cat.getParent().getName().equals(name))
                .sorted(Comparator.comparing( cats -> cats.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EntCategory> getRootCategories() {
        if(allCategories == null){
            allCategories = getAllCategories();
        }

        return Streams.stream(allCategories)
                .filter( cat -> cat.getParent() == null)
                .sorted(Comparator.comparing( cats -> cats.getName())).collect(Collectors.toList());
    }

    @Override
    public EntCategory getCategoryByName(String name) {
        if(allCategories == null){
            allCategories = getAllCategories();
        }
        return Streams.stream(allCategories).filter( cat -> cat.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "categories", sync = true)
    public Iterable<EntCategory> getAllCategories() {
        LOG.info("START retrieving all advertisement categories");
        return categoryRepo.findAll();
    }

    @Override
    public EntCategory createCategory(EntCategory category) {
        return categoryRepo.save(category);
    }
}

