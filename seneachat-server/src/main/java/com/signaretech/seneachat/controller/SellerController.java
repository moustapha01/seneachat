package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntUser;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.IUserService;
import com.signaretech.seneachat.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@SessionAttributes("currentUser")
public class SellerController {

    private final IUserService sellerService;
    private final IAdService adService;
    private final SecurityService securityService;

    @Autowired
    public SellerController(IUserService sellerService, IAdService adService, SecurityService securityService) {
        this.sellerService = sellerService;
        this.adService = adService;
        this.securityService = securityService;
    }

    @GetMapping("/web/dashboard")
    public String dashboard(Model model) {

        String loggedInUser = securityService.getLoggedInUser();
        EntUser seller = sellerService.findByEmail(loggedInUser);
        if(!(seller.getStatus().equals("A"))){
            return "registration-confirmation";
        }
        model.addAttribute("currentUser", seller.getUsername());
        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), 0, 5);//existingSeller.getAds();
        model.addAttribute("sellerAds", sellerAds);
        model.addAttribute("numPages", 3);

        return "sellerads";
    }

    @PostMapping("/web/dashboard")
    public String dashboardScroll(Model model, HttpServletRequest req) {
        String loggedInUser = securityService.getLoggedInUser();
        EntUser seller = sellerService.findByEmail(loggedInUser);
        if(!(seller.getStatus().equals("A"))){
            return "registration-confirmation";
        }
        model.addAttribute("currentUser", seller.getUsername());
        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), 0, 5);//existingSeller.getAds();
        model.addAttribute("sellerAds", sellerAds);
        model.addAttribute("listRange", req.getParameter("listRange"));
        model.addAttribute("oldRange", req.getParameter("oldRange"));
        return "sellerads";
    }

    @PostMapping("/web/seller/dashboard")
    public String getSellerAds(Model model, HttpServletRequest req) {

        HttpSession session = req.getSession();
        EntUser seller = (EntUser) session.getAttribute("user");

        String selectedPage = (String)req.getParameter("adPage");
        List<EntAdvertisement> sellerAds = adService.getSellerAds(seller.getId(), Integer.valueOf(selectedPage), 5);
        model.addAttribute("sellerAds", sellerAds);
        model.addAttribute("numPages", 3);
        return "sellerads";
    }
}
