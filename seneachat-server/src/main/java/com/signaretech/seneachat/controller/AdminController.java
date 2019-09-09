package com.signaretech.seneachat.controller;

import com.google.common.collect.Streams;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private ICategoryService categoryService;

    @Autowired
    public AdminController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/web/admin")
    public String newCategory(Model model) {
        if(!model.containsAttribute("currentCategory")){
            model.addAttribute("currentCategory", new EntCategory());
        }

        List<EntCategory> categories = Streams.stream(categoryService.getAllCategories()).collect(Collectors.toList());
        model.addAttribute("categories", categories);
        return "admin";
    }

    @PostMapping("/web/admin/save")
    public String saveCategory(@ModelAttribute("currentCategory") EntCategory currentCategory, Model model) {
        if (currentCategory.getParent() != null && currentCategory.getParent().getName() != null) {
            final EntCategory parent = categoryService.getCategoryByName(currentCategory.getParent().getName());
            currentCategory.setParent(parent);
        }
        categoryService.createCategory(currentCategory);
        List<EntCategory> categories = Streams.stream(categoryService.getAllCategories()).collect(Collectors.toList());
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", new EntCategory());
        return "admin";
    }
}

