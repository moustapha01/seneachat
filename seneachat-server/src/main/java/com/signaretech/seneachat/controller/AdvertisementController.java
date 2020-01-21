package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.model.PriceFilterEntry;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntCategory;
import com.signaretech.seneachat.persistence.entity.EntPhoto;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.ICategoryService;
import com.signaretech.seneachat.service.IUserService;
import com.signaretech.seneachat.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
@SessionAttributes({"rootCategories", "categoriesLevel2", "currentUser"})
public class AdvertisementController {

    private final ICategoryService categoryService;
    private final IAdService adService;
    private final IUserService sellerService;
    private final SecurityService securityService;

    public AdvertisementController(ICategoryService categoryService,
                                   IAdService adService,
                                   IUserService sellerService,
                                   SecurityService securityService){
        this.categoryService = categoryService;
        this.adService = adService;
        this.sellerService = sellerService;
        this.securityService = securityService;
    }

    @GetMapping("/web/dashboard/advertisements/new")
    public String newAd(Model model, HttpServletRequest req) {
        if(!model.containsAttribute("advertisement")) model.addAttribute("advertisement", new EntAdvertisement());
        List<EntCategory> rootCategories = categoryService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);
        model.addAttribute("action", "newAd");

        req.getSession().setAttribute("currAd", new EntAdvertisement());

        return "ad-new";
    }

    @PostMapping("/web/dashboard/advertisements/new")
    public String getAdSubCategories(@ModelAttribute("advertisement") EntAdvertisement advertisement, Model model, BindingResult binding,
                                     HttpServletRequest req) {


        setModelCategories(model, advertisement);
        EntAdvertisement currAd = (EntAdvertisement)req.getSession().getAttribute("currAd");
        model.addAttribute("action", "update");

//        if(currAd == null){
//            currAd = new EntAdvertisement();
//        }
//
//        advertisement.getPhotos().addAll(currAd.getPhotos());
//        req.getSession().setAttribute("currAd", advertisement);

        return "ad-new";
    }

    @PostMapping("/web/dashboard/advertisements/save")
    public ModelAndView save(@ModelAttribute("advertisement") EntAdvertisement advertisement, ModelMap model, BindingResult binding, HttpServletRequest req) {

        EntCategory category = categoryService.getCategoryByName(advertisement.getCategory().getName());
        advertisement.setCategory(category);
        String userName = securityService.getLoggedInUser();
        EntSeller seller = sellerService.findByEmail(userName);
        advertisement.setSeller(seller);

        EntAdvertisement ad = adService.updateAd(advertisement);
        model.addAttribute("currAdId", ad.getId().toString());

//        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), 0, 10);
//        model.addAttribute("sellerAds", sellerAds);
//        return "sellerads";

        model.addAttribute("action", "update");

        return new ModelAndView("redirect:/web/dashboard/advertisements/photos", model);
    }

    @PostMapping("/web/dashboard/advertisements/cancel")
    public String cancel(HttpServletRequest req) {
        return "redirect:/web/dashboard";
    }

    @PostMapping("/web/dashboard/advertisements/view")
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

    @PostMapping("/web/dashboard/advertisements/update")
    public String updateAd(@ModelAttribute("advertisement") EntAdvertisement advertisement, Model model, HttpServletRequest req){
        EntAdvertisement ad = adService.fetchAd(advertisement.getId());
        setModelCategories(model, ad);
        model.addAttribute("advertisement", ad);
        model.addAttribute("action", "update");
        return "ad-new";
    }

    @PostMapping("/web/dashboard/advertisements/delete")
    public String deleteAd(Model model, HttpServletRequest req){
        String adUuid = req.getParameter("adUuid");
        EntAdvertisement ad = adService.fetchAd(UUID.fromString(adUuid));
        adService.deleteAd(ad);

        String loggedInUser = securityService.getLoggedInUser();

        List<EntAdvertisement> sellerAds = sellerService.findByEmail(loggedInUser).getAds();
        model.addAttribute("sellerAds", sellerAds);
        return "sellerads";
    }

    @PostMapping("/web/dashboard/advertisements/{adId}/ad-photo")
    public String adPhoto(@PathVariable String adId, Model model, HttpServletRequest req) {

        EntAdvertisement currAd = null;
        if(!StringUtils.isEmpty(adId)) {
            currAd = adService.fetchAd(UUID.fromString(adId));
            EntPhoto photo = getPhotoFromBytes(req);
            if(currAd != null && photo != null) {
                photo.setAdvertisement(currAd);
                currAd.getPhotos().add(photo);
            }
            currAd = adService.updateAd(currAd);
            model.addAttribute("currAd", currAd);
        }
        return "ad-photos";
    }

    private EntPhoto getPhotoFromBytes(HttpServletRequest req) {
        EntPhoto photo = null;
        try{

            final Part filePart = req.getParts().stream().filter(p->p.getName().equals("file")).findFirst().orElse(null);
            String fileName = filePart.getSubmittedFileName();
            try(InputStream fis = filePart.getInputStream();){
                try(ByteArrayOutputStream bos = new ByteArrayOutputStream();){
                    byte[] buffer = new byte[4996];

                    int count;
                    while((count = fis.read(buffer))!=-1){
                        bos.write(buffer, 0, count);
                    }
                    byte[] photoBytes = bos.toByteArray();
                    photo = new EntPhoto();
                    photo.setImageBytes(photoBytes);
                    photo.setName(fileName);
                    photo.setPrimaryInd(false);
                }
            }

        }catch (Exception io){
            io.printStackTrace();
        }

        return photo;
    }

    @PostMapping("/web/dashboard/advertisements/{adId}/remove-photo")
    public String removePhoto(@PathVariable String adId, Model model, HttpServletRequest req) {

        String photoId = req.getParameter("photoUUID");
        adService.deletePhoto(UUID.fromString(photoId));
        EntAdvertisement currAd = null;
        if(!StringUtils.isEmpty(adId)) {
            currAd = adService.fetchAd(UUID.fromString(adId));
            model.addAttribute("currAd", currAd);
            model.addAttribute("action", "update");
        }
        return "ad-photos";
    }

    @GetMapping("/web/advertisements/categories/{category}")
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

    @GetMapping("/web/dashboard/advertisements/photos")
    public String viewPhotos(ModelMap model, HttpServletRequest req) {
        String id = (String)req.getParameter("currAdId");
        EntAdvertisement currAd = adService.fetchAd(UUID.fromString(id));
        model.addAttribute("currAd", currAd);
        model.addAttribute("action", (String)req.getParameter("action"));
        return "ad-photos";
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

