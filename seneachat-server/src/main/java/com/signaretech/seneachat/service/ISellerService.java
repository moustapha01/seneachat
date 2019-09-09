package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.AuthenticationResult;
import com.signaretech.seneachat.persistence.entity.EntSeller;

public interface ISellerService {


    /**
     * Persists the seller object to the data store.
     * @param seller, {@link EntSeller} model object to create
     */
    EntSeller createSeller(EntSeller seller);

    /**
     * Updates the seller object to the data store
     * @param seller, the {@link EntSeller} model object to update
     */
    EntSeller updateSeller(EntSeller seller);

    /**
     * @param email, email id of the seller to fetch
     * @return, the {@link EntSeller} object with email @param email.
     */
    EntSeller findByEmail(String email);

    /**
     * @param seller, a new Seller not yet activated into the system
     * @param activationCode, a generated code sent to the Seller and used to activate the new Seller
     */
    void activateAccount(EntSeller seller, String activationCode);

    void resendActivationCode(EntSeller seller);

    void register(EntSeller seller);

    AuthenticationResult authenticateUser(EntSeller user);
}
