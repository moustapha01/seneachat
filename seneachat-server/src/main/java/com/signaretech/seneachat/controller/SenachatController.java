package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("currentUser")
public class SenachatController {

    private ICategoryService categoryService;

    @Autowired
    public SenachatController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        final List<EntCategory> rootCategories = categoryService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);
        return "home";
    }
}
