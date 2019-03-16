package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SellerController {

    private ISellerService sellerService;
    private IAdService adService;

    @Autowired
    public SellerController(ISellerService sellerService, IAdService adService) {
        this.sellerService = sellerService;
        this.adService = adService;
    }

    @GetMapping("/web/seller/dashboard")
    public String dashboard(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        EntSeller seller = (EntSeller) session.getAttribute("user");

        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), 0, 5);//existingSeller.getAds();
        model.addAttribute("sellerAds", sellerAds);
        model.addAttribute("numPages", 3);

        return "sellerads";
    }

    @PostMapping("/web/seller/dashboard")
    public String getSellerAds(Model model, HttpServletRequest req) {

        HttpSession session = req.getSession();
        EntSeller seller = (EntSeller) session.getAttribute("user");

        String selectedPage = (String)req.getParameter("adPage");
        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), Integer.valueOf(selectedPage), 5);
        model.addAttribute("sellerAds", sellerAds);
        model.addAttribute("numPages", 3);
        return "sellerads";
    }
}
