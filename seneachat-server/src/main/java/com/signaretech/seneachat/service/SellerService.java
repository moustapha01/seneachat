package com.signaretech.seneachat.service;

import com.signaretech.seneachat.controller.ValidationManager;
import com.signaretech.seneachat.persistence.dao.repo.EntSellerRepo;
import com.signaretech.seneachat.persistence.entity.EntSeller;
import com.signaretech.seneachat.exception.SeneachatException;
import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.model.SellerStatus;
import com.signaretech.seneachat.util.Authentication;
import com.signaretech.seneachat.util.RandomCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.mail.internet.AddressException;

@Service
public class SellerService implements ISellerService {

    private EntSellerRepo sellerRepo;

    private IMailService mailService;

    private final TransactionTemplate transactionTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(SellerService.class);

    @Autowired
    public SellerService(IMailService mailService, EntSellerRepo sellerRepo, PlatformTransactionManager transactionManager){
        this.mailService = mailService;
        this.sellerRepo = sellerRepo;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }


    @Override
    public void createSeller(EntSeller seller) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                String activationCode = RandomCodeGenerator.generateCode();
                seller.setActivationCode(activationCode);
                seller.setStatus(SellerStatus.PENDING.getValue());
                Authentication auth = new Authentication();
                String hashCode = auth.hash(seller.getPassword().toCharArray());
                seller.setSecret(hashCode);

                sellerRepo.create(seller);
            }
        } );

    }

    @Override
    public void updateSeller(EntSeller seller) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                sellerRepo.update(seller);
            }
        });
    }

    @Override
    public EntSeller fetchSeller(String email) {
        EntSeller entSeller = transactionTemplate.execute(new TransactionCallback<EntSeller>() {
            @Override
            public EntSeller doInTransaction(TransactionStatus transactionStatus) {
                return sellerRepo.findEntSellerByEmail(email);
            }
        });
        return entSeller;
    }

    @Override
    public void activateAccount(EntSeller seller, String activationCode) throws SeneachatException {
        LOG.info("Activating account for seller {}", seller.getEmail());

        EntSeller dbSeller = fetchSeller(seller.getEmail());

        if(!StringUtils.isEmpty(activationCode) &&
                activationCode.equals(dbSeller.getActivationCode())){

            dbSeller.setStatus(SellerStatus.ACTIVE.getValue());
            updateSeller(dbSeller);
            LOG.info("Account for seller {} has been successfully activated", seller.getEmail());

        }else{
            throw new SeneachatException("Le code d'activation inscrit ne correspond pas au code d'activation que nous vous avons envoye.");
        }

    }

    @Override
    public void resendActivationCode(EntSeller seller) throws SeneachatException {

        String activationCode = RandomCodeGenerator.generateCode();
        String body = "Bienvenu sur senachat. Ci-dessous vous trouverez votre code d'activation"
                + " qui vous permettra d'activer votre compte.\n\n"
                + "code d'activation: " + activationCode
                + "\n\n"
                + "Merci.";

        EntSeller dbSeller = fetchSeller(seller.getEmail());
        dbSeller.setActivationCode(activationCode);

        try {
            updateSeller(dbSeller);
            mailService.sendMail(dbSeller.getEmail(), "Code d'Activation", body);

        } catch (AddressException adx) {
            throw new SeneachatException("Error sending email to newly registered user. Please contact support");
        } catch (javax.mail.MessagingException mex) {
            throw new SeneachatException("Error sending email to newly registered user. Please contact support");
        }
    }

    @Override
    public void register(EntSeller seller, String password2) throws SeneachatException {

        String validationResult = ValidationManager.validateInput(seller.getPassword()
                , password2, seller.getEmail(), seller.getCellPhone());

        if(validationResult.equals("valid")){

            try {

                createSeller(seller);
                EntSeller savedSeller = fetchSeller(seller.getEmail());

                String body = "Bienvenu sur senachat. Ci-dessous vous trouverez votre code d'activation"
                        + " qui vous permettra d'activer votre compte.\n\n"
                        + "code d'activation: " + savedSeller.getActivationCode()
                        + "\n\n"
                        + "Merci.";

                // mailService.sendMail(seller.getEmail(), "Code d'Activation", body);

            } /*catch (MessagingException mex) {
                LOG.info("An error occured while registering seller {} due to {}", seller.getEmail(), mex);
                throw new SenachatException(String.format("An error occurred while registering user %s. Please contact support", seller.getEmail(), mex.getMessage()));

            }*/ catch (Exception ex){
                LOG.info("An error occured while registering seller {}", seller.getEmail(), ex.getMessage());
                throw new SeneachatException(String.format("An error occurred while registering user %s. Please contact support", seller.getEmail(), ex.getMessage()));
            }
        }else{
            LOG.info("An error occured while registering seller {}. The passwords entered do not match.", seller.getEmail());
            throw new SeneachatException(String.format("An error occurred while registering user %s. The passwords entered do not match", seller.getEmail()));
        }

    }

    @Override
    public AuthenticationResult authenticateUser(EntSeller user) {

        EntSeller existingSeller = fetchSeller(user.getEmail());
        AuthenticationResult result = new AuthenticationResult(existingSeller.getStatus());

        if(existingSeller != null){
            if(user.getPassword() != null){
                Authentication auth = new Authentication();
                String passSecret = existingSeller.getSecret();
                result.setAuthenticated(auth.authenticate(user.getPassword().toCharArray(), passSecret));

                if(!result.isAuthenticated()) {
                    result.setError("Votre mot de passe est invalide.");
                }
            } else {
                result.setError("Votre mot de passe est invalide.");
            }
        } else{
            result.setError("L'addresse email fournie est invalide.");
        }

        return result;
    }
}

