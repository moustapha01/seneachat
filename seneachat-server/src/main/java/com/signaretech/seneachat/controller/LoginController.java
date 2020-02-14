package com.signaretech.seneachat.controller;

import com.google.common.collect.Lists;

import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.model.exceptions.SeneachatErrorException;
import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.service.IAdService;
import com.signaretech.seneachat.service.IUserService;
import com.signaretech.seneachat.service.SecurityService;
import com.signaretech.seneachat.service.SignareAuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class LoginController {

    private final IUserService sellerService;
    private final SecurityService securityService;

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    public LoginController(IUserService sellerService, SecurityService securityService) {
        this.sellerService = sellerService;
        this.securityService = securityService;
    }

    @GetMapping("/web/login")
    public String loginForm(Model model){
        if(!model.containsAttribute("currentUser")){
            model.addAttribute("currentUser", new EntSeller());
        }
        return "login";
    }

/*    @PostMapping("/web/login")
    public String loginSubmit(@ModelAttribute("currentUser") EntSeller currentUser, Model model, BindingResult result, HttpServletRequest req) {
        LOG.info("Login request from user {}", currentUser.getEmail());

        if(result.hasErrors()){
            return "login";
        }

        Authentication authResult = null;

        try{
            authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    currentUser.getEmail(), currentUser.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authResult);
        }catch (BadCredentialsException bdex) {
            req.setAttribute("errorMs", bdex.getMessage());
        }

        EntSeller existingSeller = sellerService.findByEmail(currentUser.getEmail());

        if(authResult.isAuthenticated() && existingSeller.getStatus().equals("A")){
            model.addAttribute("currentUser", existingSeller);
            req.getSession().setAttribute("user", existingSeller);
            List<EntAdvertisement> sellerAds = adService.getSellerAds(existingSeller.getId(), 0, 10);//existingSeller.getAds();
            model.addAttribute("sellerAds", sellerAds);
            return "redirect:/web/dashboard";
        }
        else if(authResult.isAuthenticated() && !existingSeller.getStatus().equals("A")){
            return "registration-confirmation";
        }

        return "login";
    }*/

    @GetMapping("/web/register")
    public String register(Model model){
        if(!model.containsAttribute("currentUser")) model.addAttribute("currentUser", new EntSeller());
        return "register";
    }

    @PostMapping("/web/register")
    public String register(@ModelAttribute("currentUser") @Valid EntSeller currentUser,
                           BindingResult bindingResult,
                           Model model,
                           HttpServletRequest req){

        LOG.info("New user registration request from {}", currentUser.getUsername());
        String password2 = req.getParameter("password2");
        if(bindingResult.hasErrors()){
            return "register";
        }
        else{
            try {
                sellerService.register(currentUser);
            } catch (Exception ex) {
                req.setAttribute("errorMsg", ex.getMessage());
                return "register";
            }
        }

        return "registration-confirmation";
    }

    @PostMapping("/web/activate")
    public String activateAccount(@ModelAttribute("currentUser") EntSeller currentUser, HttpServletRequest req, Model model){
        String activationCode = req.getParameter("activationCode");
        String email = securityService.getLoggedInUser();
        try{
            sellerService.activateAccount(email, activationCode);
        }catch (SeneachatErrorException se){
            model.addAttribute("errorMsg", se.getMessage());
            return "registration-confirmation";
        }

        model.addAttribute("sellerAds", null);
        return "sellerads";
    }


    @PostMapping("/web/resendactivationcode")
    public String resendActivationCode(Model model, HttpServletRequest req){

        HttpSession session = req.getSession();
        EntSeller currentSeller = (EntSeller) session.getAttribute("currentSeller");

            sellerService.resendActivationCode(currentSeller);



        return "registration_confirmation";
    }


}

