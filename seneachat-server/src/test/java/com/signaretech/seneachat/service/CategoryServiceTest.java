package com.signaretech.seneachat.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.signaretech.seneachat.SeneachatApplication;
import com.signaretech.seneachat.config.TestConfig;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.persistence.utils.UUIDUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SeneachatApplication.class, TestConfig.class})
@ActiveProfiles("junit")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryServiceTest {

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void testGenerateUuid() {
        IntStream.range(0, 100).forEach( i -> System.out.println(UUID.randomUUID()) );
    }

    @Test
    public void testCreateNestedCategories() {
        EntCategory cat1 = createAndSaveCategory("Electroniques", null);
        EntCategory cat11 = createAndSaveCategory("Televisions", cat1);

        EntCategory cat2 = createAndSaveCategory("Cellulaires", null);
        EntCategory cat21 = createAndSaveCategory("Android", cat2);
        EntCategory cat22 = createAndSaveCategory("Iphone", cat2);

        EntCategory cat221 = createAndSaveCategory("IPhone 7", cat22);
        EntCategory cat222 = createAndSaveCategory("IPhone 7S Plus", cat22);
        EntCategory cat223 = createAndSaveCategory("IPhone XR", cat22);

        Iterable<EntCategory> categories = categoryService.getAllCategories();
        Assert.assertEquals(Iterables.size(categories), 8);

        Iterable<EntCategory> categories2 = categoryService.getCategoriesByParent(cat22.getName());
        Assert.assertEquals(3, ((List<EntCategory>) categories2).size());
       // Iterable<EntCategory> categories3 = categoryService.getAllCategories();
    }

    private EntCategory createAndSaveCategory(String name, EntCategory parent) {
        EntCategory category = new EntCategory(name);
        category.setParent(parent);
        return categoryService.createCategory(category);
    }
}