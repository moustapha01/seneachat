package com.signaretech.seneachat.service;

import com.signaretech.seneachat.exception.SeneachatException;
import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.model.SellerDTO;

import java.sql.SQLException;

public interface ISellerService {

    /**
     * @param seller, {@link SellerDTO} object to be created
     * @throws SQLException
     */
    void createSeller(SellerDTO seller);

    void updateSeller(SellerDTO seller);

    /**
     * @param email, email id of the {@link SellerDTO}
     * @return, the {@link SellerDTO} object with email @param email.
     */
    SellerDTO fetchSeller(String email);

    void activateAccount(SellerDTO seller, String activationCode) throws SeneachatException;

    void resendActivationCode(SellerDTO seller) throws SeneachatException;

    void register(SellerDTO seller, String password2) throws SeneachatException;

    AuthenticationResult authenticateUser(SellerDTO user);
}
