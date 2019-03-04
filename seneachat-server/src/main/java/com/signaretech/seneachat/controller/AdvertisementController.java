package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.model.AdvertisementDTO;
import com.signaretech.seneachat.model.CategoryDTO;
import com.signaretech.seneachat.model.PhotoDTO;
import com.signaretech.seneachat.model.SellerDTO;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.ICategoryService;
import com.signaretech.seneachat.service.ISellerService;
import com.signaretech.seneachat.util.AdvertisementValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
public class AdvertisementController {

    private ICategoryService categoryService;
    private IAdService adService;
    private ISellerService sellerService;

    @Autowired
    public AdvertisementController(ICategoryService categoryService, IAdService adService, ISellerService sellerService){
        this.categoryService = categoryService;
        this.adService = adService;
        this.sellerService = sellerService;
    }

    @GetMapping("/web/advertisements/new")
    public String newAd(Model model, HttpServletRequest req) {
        if(!model.containsAttribute("advertisement")) model.addAttribute("advertisement", new AdvertisementDTO());
        List<CategoryDTO> rootCategories = categoryService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);
        model.addAttribute("action", "newAd");

        req.getSession().setAttribute("currAd", new AdvertisementDTO());

        return "ad-new";
    }

    @PostMapping("/web/advertisements/new")
    public String getAdSubCategories(@ModelAttribute("advertisement") AdvertisementDTO advertisement, Model model, BindingResult binding,
                                     HttpServletRequest req) {

        setModelCategories(model, advertisement);

        AdvertisementDTO currAd = (AdvertisementDTO)req.getSession().getAttribute("currAd");

        if(currAd == null){
            currAd = new AdvertisementDTO();
        }

        advertisement.getPhotos().addAll(currAd.getPhotos());

        req.getSession().setAttribute("currAd", advertisement);

        return "ad-new";
    }

    @PostMapping("/web/advertisements/save")
    public String save(@ModelAttribute("advertisement") AdvertisementDTO advertisement, Model model, BindingResult binding,
                       HttpServletRequest req) {

        AdvertisementDTO currAd = (AdvertisementDTO) req.getSession().getAttribute("currAd");

        if(AdvertisementValidation.validateAdvertisement(currAd)){

            SellerDTO sellerDTO = (SellerDTO) req.getSession().getAttribute("currentUser");
            currAd.setSeller(sellerDTO);

            if(currAd.getId() == null){
                adService.createAd(currAd);
            }else{
                adService.updateAd(currAd);
            }
            //AdvertisementDTO ad = adService.createAd(currAd);

            List<AdvertisementDTO> sellerAds = sellerService.fetchSeller(sellerDTO.getEmail()).getAds();
            model.addAttribute("sellerAds", sellerAds);
            return "sellerads";
        }
        else{
            return "ad-new";
        }
    }

    @PostMapping("/web/advertisements/cancel")
    public String cancel(HttpServletRequest req) {
        return "redirect:/web/seller/dashboard";
    }

    @PostMapping("/web/advertisements/view")
    public String viewAd(Model model, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");

        AdvertisementDTO ad = adService.fetchAd(UUID.fromString(adUuid));

        req.getSession().setAttribute("currAd", ad);
        setModelCategories(model, ad);
        model.addAttribute("advertisement", ad);
        model.addAttribute("action", "view");
        return "ad-new";
    }

    @PostMapping("/web/advertisements/update")
    public String updateAd(Model model, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");

       // AdvertisementDTO ad = adService.fetchAd(UUID.fromString(adUuid));

        AdvertisementDTO ad = (AdvertisementDTO)req.getSession().getAttribute("currAd");
       // req.getSession().setAttribute("currAd", ad);
        setModelCategories(model, ad);
        model.addAttribute("advertisement", ad);
        model.addAttribute("action", "update");
        return "ad-new";
    }

    @PostMapping("/web/advertisements/delete")
    public String deleteAd(Model model, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");

        AdvertisementDTO ad = adService.fetchAd(UUID.fromString(adUuid));

        adService.deleteAd(ad);

        SellerDTO sellerDTO = (SellerDTO) req.getSession().getAttribute("currentUser");

        List<AdvertisementDTO> sellerAds = sellerService.fetchSeller(sellerDTO.getEmail()).getAds();
        model.addAttribute("sellerAds", sellerAds);

        return "sellerads";
    }

    @PostMapping("/web/advertisements/ad-photo")
    public String adPhoto(Model model, HttpServletRequest req) {

        AdvertisementDTO currAd = (AdvertisementDTO) req.getSession().getAttribute("currAd");

        if(currAd == null) {
            currAd = new AdvertisementDTO();
        }

        SellerDTO sellerDTO = (SellerDTO)req.getSession().getAttribute("currentUser");
        currAd.setSeller(sellerService.fetchSeller(sellerDTO.getEmail()));

        setModelCategories(model, currAd);

        try{

            final Part filePart = req.getParts().iterator().next();
            String fileName = filePart.getSubmittedFileName();
            try(InputStream fis = filePart.getInputStream();){
                try( ByteArrayOutputStream bos = new ByteArrayOutputStream();){
                    byte[] buffer = new byte[4996];

                    int count;
                    while((count = fis.read(buffer))!=-1){
                        bos.write(buffer, 0, count);
                    }
                    byte[] photoBytes = bos.toByteArray();
                    PhotoDTO photo = new PhotoDTO();
                    photo.setImageBytes(photoBytes);
                    photo.setName(fileName);
                    currAd.getPhotos().add(photo);
                    //                  AdvertisementDTO savedAd = adService.createAd(advertisement);
                    req.getSession().setAttribute("currAd", currAd);
                    model.addAttribute("advertisement", currAd);
                }
            }

        }catch (Exception io){
            io.printStackTrace();
        }

        //adService.createAd(advertisement);

        return "ad-new";

    }

    @PostMapping("/web/advertisements/remove-photo")
    public String removePhoto(Model model, HttpServletRequest req) {
        AdvertisementDTO currAd = (AdvertisementDTO) req.getSession().getAttribute("currAd");
        String photoId = req.getParameter("photoUUID");

        currAd.getPhotos().remove(Integer.valueOf(photoId));

        setModelCategories(model, currAd);
        model.addAttribute("advertisement", currAd);
        req.getSession().setAttribute("currAd", currAd);

        return "ad-new";
    }

    private void setModelCategories(Model model, AdvertisementDTO advertisement) {
        List<CategoryDTO> rootCategories = categoryService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);

        List<CategoryDTO> categoriesLevel2 = categoryService.getCategoriesByParent(advertisement.getCategory().getCategoryLevel1());
        model.addAttribute("categoriesLevel2", categoriesLevel2);

        List<CategoryDTO> categoriesLevel3 = categoryService.getCategoriesByParent(advertisement.getCategory().getCategoryLevel2());
        model.addAttribute("categoriesLevel3", categoriesLevel3);

        boolean showCategoryLevel2 = (categoriesLevel2 != null && !categoriesLevel2.isEmpty()) ? true : false;
        model.addAttribute("showCategoryLevel2", showCategoryLevel2);

        boolean showCategoryLevel3 = (categoriesLevel3 != null && !categoriesLevel3.isEmpty()) ? true : false;
        model.addAttribute("showCategoryLevel3", showCategoryLevel3);
    }
}

