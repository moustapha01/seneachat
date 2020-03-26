package com.signaretech.seneachat.service;

import com.signaretech.seneachat.MessageTranslator;
import com.signaretech.seneachat.common.validation.EntityValidator;
import com.signaretech.seneachat.model.exceptions.SeneachatErrorException;
import com.signaretech.seneachat.persistence.dao.repo.EntUserRepo;
import com.signaretech.seneachat.persistence.entity.EntUser;
import com.signaretech.seneachat.model.SellerStatus;
import com.signaretech.seneachat.util.RandomCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final EntUserRepo userRepo;
    private final IMailService mailService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final EntityValidator entityValidator = new EntityValidator();

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService(IMailService mailService, EntUserRepo userRepo, BCryptPasswordEncoder passwordEncoder){
        this.mailService = mailService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    //@Transactional
    public EntUser createSeller(EntUser user) {
        String activationCode = RandomCodeGenerator.generateCode();
        LOG.info("Generated activation code is {}", activationCode);
        user.setActivationCode(activationCode);
        user.setStatus(SellerStatus.PENDING.getValue());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setCreatedBy("SYSTEM");
        user.setLastModifiedBy("SYSTEM");
        user.setCreatedDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());

        return this.userRepo.save(user);
    }

    @Override
    @Transactional
    public EntUser updateSeller(EntUser seller) {
        return userRepo.save(seller);
    }

    @Override
    public EntUser findByEmail(String email) {
        return userRepo.findByUsername(email);
    }

    @Override
    public EntUser findById(UUID id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void activateAccount(String userName, String activationCode) {
        LOG.info("Activating account for seller {}", userName);

        EntUser dbSeller = findByEmail(userName);

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
    public void resendActivationCode(String username) {

        String activationCode = RandomCodeGenerator.generateCode();
        String body = MessageTranslator.getLocaleMessage("registration.confirmation") + activationCode;
        LOG.info("Activation code is: {}", activationCode);

        EntUser dbSeller = findByEmail(username);
        dbSeller.setActivationCode(activationCode);

        try {
            updateSeller(dbSeller);
            mailService.sendMail(dbSeller.getUsername(), "Code d'Activation", body);

        } catch (AddressException adx) {
            //throw new SeneachatErrorException("Error sending email to newly registered user. Please contact support");
        } catch (javax.mail.MessagingException mex) {
            //throw new SeneachatErrorException("Error sending email to newly registered user. Please contact support");
        }
    }

    @Override
    public void register(EntUser seller) {

        try {
            entityValidator.validate(seller);
            if (userRepo.existsByUsername(seller.getUsername())){
                throw new SeneachatErrorException(MessageTranslator.getLocaleMessage("email.exist"));
            }

            EntUser savedSeller = createSeller(seller);
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
}

