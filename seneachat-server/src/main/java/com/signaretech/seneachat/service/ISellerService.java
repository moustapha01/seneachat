package com.signaretech.seneachat.service;

import com.signaretech.seneachat.exception.SeneachatException;
import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.persistence.entity.EntSeller;

import java.sql.SQLException;

public interface ISellerService {

    /**
     * @param seller, {@link EntSeller} object to be created
     * @throws SQLException
     */
    void createSeller(EntSeller seller);

    void updateSeller(EntSeller seller);

    /**
     * @param email, email id of the {@link EntSeller}
     * @return, the {@link EntSeller} object with email @param email.
     */
    EntSeller fetchSeller(String email);

    void activateAccount(EntSeller seller, String activationCode) throws SeneachatException;

    void resendActivationCode(EntSeller seller) throws SeneachatException;

    void register(EntSeller seller, String password2) throws SeneachatException;

    AuthenticationResult authenticateUser(EntSeller user);
}
