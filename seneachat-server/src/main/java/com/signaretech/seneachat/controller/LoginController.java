package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.MessageTranslator;
import com.signaretech.seneachat.model.exceptions.SeneachatErrorException;
import com.signaretech.seneachat.persistence.entity.EntUser;
import com.signaretech.seneachat.service.IUserService;
import com.signaretech.seneachat.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
            model.addAttribute("currentUser", new EntUser());
        }
        return "login";
    }

    @GetMapping("/web/loginFailure")
    public String loginFailure(Model model) {
        if(!model.containsAttribute("currentUser")){
            model.addAttribute("currentUser", new EntUser());
        }
        String msg = MessageTranslator.getLocaleMessage("invalid.login");
        model.addAttribute("errorMsg", msg);
        return "login";
    }

    @GetMapping("/web/register")
    public String register(Model model){
        if(!model.containsAttribute("currentUser")) model.addAttribute("currentUser", new EntUser());
        return "register";
    }

    @PostMapping("/web/register")
    public String register(@ModelAttribute("currentUser") @Valid EntUser currentUser,
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
                model.addAttribute("username", currentUser.getUsername());
            } catch (Exception ex) {
                req.setAttribute("errorMsg", ex.getMessage());
                return "register";
            }
        }

        return "registration-confirmation";
    }

    @PostMapping("/web/activate")
    public String activateAccount(HttpServletRequest req, Model model){
        String activationCode = req.getParameter("activationCode");
        String username = req.getParameter("username");
        String email = securityService.getLoggedInUser() == null ? username : null;

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
    public String resendActivationCode (HttpServletRequest req, Model model){
        String username = req.getParameter("username");
        String email = securityService.getLoggedInUser() == null ? username : null;
        sellerService.resendActivationCode(email);
        model.addAttribute("username", username);
        return "registration-confirmation";
    }

}
