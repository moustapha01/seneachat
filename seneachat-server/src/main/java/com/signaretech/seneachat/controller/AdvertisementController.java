package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.model.PriceFilterEntry;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.persistence.entity.EntPhoto;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.persistence.utils.UUIDUtil;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.ICategoryService;
import com.signaretech.seneachat.service.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
@SessionAttributes({"rootCategories", "categoriesLevel2", "currentUser"})
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
        if(!model.containsAttribute("advertisement")) model.addAttribute("advertisement", new EntAdvertisement());
        List<EntCategory> rootCategories = categoryService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);
        model.addAttribute("action", "newAd");

        req.getSession().setAttribute("currAd", new EntAdvertisement());

        return "ad-new";
    }

    @PostMapping("/web/advertisements/new")
    public String getAdSubCategories(@ModelAttribute("advertisement") EntAdvertisement advertisement, Model model, BindingResult binding,
                                     HttpServletRequest req) {

        setModelCategories(model, advertisement);
        EntAdvertisement currAd = (EntAdvertisement)req.getSession().getAttribute("currAd");

        if(currAd == null){
            currAd = new EntAdvertisement();
        }

        advertisement.getPhotos().addAll(currAd.getPhotos());

        req.getSession().setAttribute("currAd", advertisement);

        return "ad-new";
    }

    @PostMapping("/web/advertisements/save")
    public String save(@ModelAttribute("advertisement") EntAdvertisement advertisement, ModelMap model, BindingResult binding, HttpServletRequest req) {

        EntCategory category = categoryService.getCategoryByName(advertisement.getCategory().getName());
        advertisement.setCategory(category);
        EntSeller seller = (EntSeller) model.get("currentUser");
        advertisement.setSeller(seller);

        adService.updateAd(advertisement);

        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), 0, 10);
        model.addAttribute("sellerAds", sellerAds);
        return "sellerads";
    }

    @PostMapping("/web/advertisements/cancel")
    public String cancel(HttpServletRequest req) {
        return "redirect:/web/seller/dashboard";
    }

    @PostMapping("/web/advertisements/view")
    public String viewAd(Model model, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");

        EntAdvertisement ad = adService.fetchAd(UUID.fromString(adUuid));
        setModelCategories(model, ad);
        model.addAttribute("advertisement", ad);
        model.addAttribute("action", "view");
        return "ad-new";
    }

    @GetMapping("/web/advertisements/view/{adId}")
    public String viewAdDetails(Model model, @PathVariable String adId, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");

        EntAdvertisement ad = adService.fetchAd(UUID.fromString(adId));
        model.addAttribute("advertisement", ad);
        model.addAttribute("selectedId", ad.getPhotos().get(0).getId());
        return "ad-detail";
    }

    @PostMapping("/web/advertisements/update")
    public String updateAd(@ModelAttribute("advertisement") EntAdvertisement advertisement, Model model, HttpServletRequest req){
        EntAdvertisement ad = adService.fetchAd(advertisement.getId());
        setModelCategories(model, ad);
        model.addAttribute("advertisement", ad);
        model.addAttribute("action", "update");
        return "ad-new";
    }

    @PostMapping("/web/advertisements/delete")
    public String deleteAd(Model model, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");
        EntAdvertisement ad = adService.fetchAd(UUID.fromString(adUuid));
        adService.deleteAd(ad);

        EntSeller sellerDTO = (EntSeller) req.getSession().getAttribute("currentUser");

        List<EntAdvertisement> sellerAds = sellerService.findByEmail(sellerDTO.getEmail()).getAds();
        model.addAttribute("sellerAds", sellerAds);
        return "sellerads";
    }

    @PostMapping("/web/advertisements/ad-photo")
    public String adPhoto(Model model, HttpServletRequest req) {

        String adUuid = req.getParameter("adId");
        EntAdvertisement currAd = adService.fetchAd(UUID.fromString(adUuid));

        EntSeller seller = (EntSeller) model.asMap().get("currentUser");
    //    EntSeller dbSeller = sellerService.findById(seller.getId());
        currAd.setSeller(seller);

        if(currAd == null) {
            currAd = new EntAdvertisement();
        }

        setModelCategories(model, currAd);

        try{

            final Part filePart = req.getParts().iterator().next();
            String fileName = filePart.getSubmittedFileName();
            try(InputStream fis = filePart.getInputStream();){
                try(ByteArrayOutputStream bos = new ByteArrayOutputStream();){
                    byte[] buffer = new byte[4996];

                    int count;
                    while((count = fis.read(buffer))!=-1){
                        bos.write(buffer, 0, count);
                    }
                    byte[] photoBytes = bos.toByteArray();
                    EntPhoto photo = new EntPhoto();
                    photo.setImageBytes(photoBytes);
                    photo.setName(fileName);
                    photo.setAdvertisement(currAd);
                    currAd.getPhotos().add(photo);
                    photo.setPrimaryInd(false);
                    EntAdvertisement savedAd = adService.updateAd(currAd);
                    model.addAttribute("advertisement", currAd);
                }
            }

        }catch (Exception io){
            io.printStackTrace();
        }

        return "ad-new";
    }

    @PostMapping("/web/advertisements/remove-photo")
    public String removePhoto(Model model, HttpServletRequest req) {
        EntAdvertisement currAd = (EntAdvertisement) req.getSession().getAttribute("currAd");
        String photoId = req.getParameter("photoUUID");

        currAd.getPhotos().remove(Integer.valueOf(photoId));

        setModelCategories(model, currAd);
        model.addAttribute("advertisement", currAd);
        req.getSession().setAttribute("currAd", currAd);

        return "ad-new";
    }

    @GetMapping("/web/advertisements/{category}")
    public String retrieveAdCategories(Model model, @PathVariable String category, HttpServletRequest req) {

        List<EntAdvertisement> ads = adService.getCategoryAds(category);
        List<PriceFilterEntry> priceFilters = adService.getPriceFilters(ads);
        final List<EntCategory> rootCategories = categoryService.getRootCategories();
        final List<EntCategory> childCategories = categoryService.getCategoriesByParent(category);

        model.addAttribute("advertisements", ads);
        model.addAttribute("priceFilters", priceFilters);
        model.addAttribute("rootCategories", rootCategories);
        model.addAttribute("childCategories", childCategories);

        return "category-ads";
    }

    private void setModelCategories(Model model, EntAdvertisement advertisement) {
        List<EntCategory> rootCategories = categoryService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);

        if(advertisement.getCategory().getParent() == null) {
            EntCategory parent = categoryService.getCategoryByName(advertisement.getCategory().getName());
            advertisement.getCategory().setParent(parent);
            List<EntCategory> categoriesLevel2 = categoryService.getCategoriesByParent(advertisement.getCategory().getName());
            model.addAttribute("categoriesLevel2", categoriesLevel2);
        }
        else if (advertisement.getCategory().getParent().getParent() == null) {
            EntCategory parent = categoryService.getCategoryByName(advertisement.getCategory().getParent().getName());
            advertisement.getCategory().setParent(parent);
            List<EntCategory> categoriesLevel2 = categoryService.getCategoriesByParent(advertisement.getCategory().getParent().getName());
            model.addAttribute("categoriesLevel2", categoriesLevel2);
        }
    }
}

