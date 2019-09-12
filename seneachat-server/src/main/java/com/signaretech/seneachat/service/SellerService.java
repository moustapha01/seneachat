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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.ConstraintViolationException;
import java.util.UUID;

@Service
public class SellerService implements ISellerService {

    private EntSellerRepo sellerRepo;
    private IMailService mailService;

    private final EntityValidator entityValidator = new EntityValidator();

    private static final Logger LOG = LoggerFactory.getLogger(SellerService.class);

    @Autowired
    public SellerService(IMailService mailService, EntSellerRepo sellerRepo){
        this.mailService = mailService;
        this.sellerRepo = sellerRepo;
    }


    @Override
    //@Transactional
    public EntSeller createSeller(EntSeller seller) {
        String activationCode = RandomCodeGenerator.generateCode();
        seller.setActivationCode(activationCode);
        seller.setStatus(SellerStatus.PENDING.getValue());
        Authentication auth = new Authentication();
        String hashCode = auth.hash(seller.getPassword().toCharArray());
        seller.setSecret(hashCode);
        return sellerRepo.save(seller);
    }

    @Override
   // @Transactional
    public EntSeller updateSeller(EntSeller seller) {
        return sellerRepo.save(seller);
    }

    @Override
    public EntSeller findByEmail(String email) {
        return sellerRepo.findByEmail(email);
    }

    @Override
    public EntSeller findById(UUID id) {
        return sellerRepo.findById(id).orElse(null);
    }

    @Override
    public void activateAccount(EntSeller seller, String activationCode) {
        LOG.info("Activating account for seller {}", seller.getEmail());

        EntSeller dbSeller = findByEmail(seller.getEmail());

        if(!StringUtils.isEmpty(activationCode) &&
                activationCode.equals(dbSeller.getActivationCode())){

            dbSeller.setStatus(SellerStatus.ACTIVE.getValue());
            updateSeller(dbSeller);
            LOG.info("Account for seller {} has been successfully activated", seller.getEmail());

        }else{
            throw new SeneachatErrorException(MessageTranslator.getLocaleMessage("activationcode.invalid"));
        }

    }

    @Override
    public void resendActivationCode(EntSeller seller) {

        String activationCode = RandomCodeGenerator.generateCode();
        String body = MessageTranslator.getLocaleMessage("registration.confirmation") + activationCode;

        EntSeller dbSeller = findByEmail(seller.getEmail());
        dbSeller.setActivationCode(activationCode);

        try {
            updateSeller(dbSeller);
            mailService.sendMail(dbSeller.getEmail(), "Code d'Activation", body);

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
            if (sellerRepo.existsByEmail(seller.getEmail())){
                throw new SeneachatErrorException(MessageTranslator.getLocaleMessage("email.exist"));
            }

            EntSeller savedSeller = createSeller(seller);
            String body = MessageTranslator.getLocaleMessage("registration.confirmation") + savedSeller.getActivationCode();
            mailService.sendMail(seller.getEmail(), "Code d'Activation", body);

        } catch (ConstraintViolationException vex) {
            throw new SeneachatErrorException(String.format(
                    "The EntitySeller object passed to register is invalid due to: %s",
                    vex.getConstraintViolations().toString()));

        }catch (MessagingException mex) {
                LOG.info("An error occured while registering seller {} due to {}", seller.getEmail(), mex);

        } catch (Exception ex) {
            LOG.info("An error occured while registering seller {}", seller.getEmail(), ex.getMessage());
            throw new SeneachatErrorException(String.format(
                    "The following error occurred while registering user %s. %s. Please contact support",
                    seller.getEmail(), ex.getMessage()));
        }

    }

    @Override
    public AuthenticationResult authenticateUser(EntSeller user) {

        EntSeller existingSeller = findByEmail(user.getEmail());
        AuthenticationResult result = new AuthenticationResult(existingSeller.getStatus());

        if(existingSeller != null){
            if(user.getPassword() != null){
                Authentication auth = new Authentication();
                String passSecret = existingSeller.getSecret();
                result.setAuthenticated(auth.authenticate(user.getPassword().toCharArray(), passSecret));

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
}

