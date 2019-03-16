package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.exception.SeneachatException;

import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.ISellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@SessionAttributes("currentUser")
public class LoginController {

    @Autowired
    private ISellerService sellerService;

    @Autowired
    private IAdService adService;

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/web/login")
    public String loginForm(Model model){
        if(!model.containsAttribute("currentUser")){
            model.addAttribute("currentUser", new EntSeller());
        }
        return "login";
    }

    @PostMapping("/web/login")
    public String loginSubmit(@ModelAttribute("currentUser") EntSeller currentUser, Model model, BindingResult result, HttpServletRequest req) {
        LOG.info("Login request from user {}", currentUser.getEmail());

        if(result.hasErrors()){
            return "login";
        }

        AuthenticationResult authResult = sellerService.authenticateUser(currentUser);

        if(authResult.isAuthenticated() && authResult.getSellerStatus().equals("A")){
            EntSeller existingSeller = sellerService.fetchSeller(currentUser.getEmail());
            req.getSession().setAttribute("user", existingSeller);
//            List<AdvertisementDTO> sellerAds = adService.getSellerAds(existingSeller.getId());//existingSeller.getAds();
//            model.addAttribute("sellerAds", sellerAds);
//            model.addAttribute("numPages", 3);
            return "redirect:/web/seller/dashboard";
        }
        else if(authResult.isAuthenticated() && !authResult.getSellerStatus().equals("A")){
            return "registration-confirmation";
        }

        req.setAttribute("errorMsg", authResult.getError());
        return "login";
    }

    @GetMapping("/web/register")
    public String register(Model model){
        if(!model.containsAttribute("currentUser")) model.addAttribute("currentUser", new EntSeller());
        return "register";
    }

    @PostMapping("/web/register")
    public String register(@ModelAttribute("currentUser") EntSeller currentUser, Model model, BindingResult result,
                           HttpServletRequest req){

        LOG.info("New user registration request from {}", currentUser.getEmail());
        String password2 = req.getParameter("password2");

        try {
            sellerService.register(currentUser, password2);
        } catch (Exception ex) {
            req.setAttribute("errorMsg", ex.getMessage());
            return "register";
        }

        req.getSession().setAttribute("sellerEmail", currentUser.getEmail());

        return "registration-confirmation";
    }

    @PostMapping("/web/activate")
    public String activateAccount(@ModelAttribute("currentUser") EntSeller currentUser, HttpServletRequest req, Model model){

        List<EntAdvertisement> sellerAds = null;
        String activationCode = req.getParameter("activationCode");
//        String email = (String) req.getSession().getAttribute("sellerEmail");
        String email = currentUser.getEmail();
//        currentUser.setEmail(email);
        try {
            sellerService.activateAccount(currentUser, activationCode);
            model.addAttribute("sellerAds", sellerAds);
        } catch (SeneachatException se) {
            LOG.error("Error while activating account for seller {}.", currentUser.getEmail(), se.getMessage());
            req.setAttribute("errorMsg", se.getMessage());
            return "registration-confirmation";
        }
        return "sellerads";
    }


    @PostMapping("/web/resendactivationcode")
    public String resendActivationCode(Model model, HttpServletRequest req){

        HttpSession session = req.getSession();
        EntSeller currentSeller = (EntSeller) session.getAttribute("currentSeller");

        try {
            sellerService.resendActivationCode(currentSeller);
        } catch (SeneachatException se) {
            model.addAttribute("errorMsg", se.getMessage());
        }

        return "registration_confirmation";
    }


}

