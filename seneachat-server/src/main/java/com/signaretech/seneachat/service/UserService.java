package com.signaretech.seneachat.service;

import com.signaretech.seneachat.MessageTranslator;
import com.signaretech.seneachat.common.validation.EntityValidator;
import com.signaretech.seneachat.model.exceptions.SeneachatErrorException;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.model.SellerStatus;
import com.signaretech.seneachat.util.Authentication;
import com.signaretech.seneachat.util.RandomCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements IUserService, UserDetailsService {

    private final EntSellerRepo sellerRepo;
    private final IMailService mailService;
    private final BCryptPasswordEncoder passwordEncoder;


    private final EntityValidator entityValidator = new EntityValidator();

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(IMailService mailService, EntSellerRepo sellerRepo, BCryptPasswordEncoder passwordEncoder){
        this.mailService = mailService;
        this.sellerRepo = sellerRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    //@Transactional
    public EntSeller createSeller(EntSeller seller) {
        String activationCode = RandomCodeGenerator.generateCode();
        seller.setActivationCode(activationCode);
        seller.setStatus(SellerStatus.PENDING.getValue());
        Authentication auth = new Authentication();
        String hashCode = auth.hash(seller.getPassword().toCharArray());
        seller.setSecret(passwordEncoder.encode(seller.getPassword()));
        return sellerRepo.save(seller);
    }

    @Override
   // @Transactional
    public EntSeller updateSeller(EntSeller seller) {
        return sellerRepo.save(seller);
    }

    @Override
    public EntSeller findByEmail(String email) {
        return sellerRepo.findByUsername(email);
    }

    @Override
    public EntSeller findById(UUID id) {
        return sellerRepo.findById(id).orElse(null);
    }

    @Override
    public void activateAccount(String userName, String activationCode) {
        LOG.info("Activating account for seller {}", userName);

        EntSeller dbSeller = findByEmail(userName);

        if(!StringUtils.isEmpty(activationCode) &&
                activationCode.equals(dbSeller.getActivationCode())){

            dbSeller.setStatus(SellerStatus.ACTIVE.getValue());
            updateSeller(dbSeller);
            LOG.info("Account for seller {} has been successfully activated", userName);

        }else{
            throw new SeneachatErrorException(MessageTranslator.getLocaleMessage("activationcode.invalid"));
        }

    }

    @Override
    public void resendActivationCode(EntSeller seller) {

        String activationCode = RandomCodeGenerator.generateCode();
        String body = MessageTranslator.getLocaleMessage("registration.confirmation") + activationCode;

        EntSeller dbSeller = findByEmail(seller.getUsername());
        dbSeller.setActivationCode(activationCode);

        try {
            updateSeller(dbSeller);
            mailService.sendMail(dbSeller.getUsername(), "Code d'Activation", body);

        } catch (AddressException adx) {
            throw new SeneachatErrorException("Error sending email to newly registered user. Please contact support");
        } catch (javax.mail.MessagingException mex) {
            throw new SeneachatErrorException("Error sending email to newly registered user. Please contact support");
        }
    }

    @Override
    public void register(EntSeller seller) {

        try {
            entityValidator.validate(seller);
            if (sellerRepo.existsByUsername(seller.getUsername())){
                throw new SeneachatErrorException(MessageTranslator.getLocaleMessage("email.exist"));
            }

            EntSeller savedSeller = createSeller(seller);
            String body = MessageTranslator.getLocaleMessage("registration.confirmation") + savedSeller.getActivationCode();
            mailService.sendMail(seller.getUsername(), "Code d'Activation", body);

        } catch (ConstraintViolationException vex) {
            throw new SeneachatErrorException(String.format(
                    "The EntitySeller object passed to register is invalid due to: %s",
                    vex.getConstraintViolations().toString()));

        }catch (MessagingException mex) {
                LOG.info("An error occured while registering seller {} due to {}", seller.getUsername(), mex);

        } catch (Exception ex) {
            LOG.info("An error occured while registering seller {}", seller.getUsername(), ex.getMessage());
            throw new SeneachatErrorException(String.format(
                    "The following error occurred while registering user %s. %s. Please contact support",
                    seller.getUsername(), ex.getMessage()));
        }

    }

    @Override
    public AuthenticationResult authenticateUser(String userName, String password) {

        EntSeller existingSeller = findByEmail(userName);
        AuthenticationResult result = new AuthenticationResult(existingSeller.getStatus());

        if(existingSeller != null){
            if(password != null){
                Authentication auth = new Authentication();
                String passSecret = existingSeller.getSecret();
                result.setAuthenticated(auth.authenticate(password.toCharArray(), passSecret));

                if(!result.isAuthenticated()) {
                    result.setError(MessageTranslator.getLocaleMessage("password.invalid"));
                }
            } else {
                result.setError(MessageTranslator.getLocaleMessage("password.invalid"));
            }
        } else{
            result.setError(MessageTranslator.getLocaleMessage("email.invalid"));
        }

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        EntSeller user = sellerRepo.findByUsername(userName);
        if(user == null) throw new UsernameNotFoundException(userName);

        return new User(user.getUsername(), user.getSecret(), Collections.singleton(new SimpleGrantedAuthority("SELLER")));
    }
}

