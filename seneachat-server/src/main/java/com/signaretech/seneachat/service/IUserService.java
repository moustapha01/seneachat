package com.signaretech.seneachat.service;

import com.signaretech.seneachat.persistence.entity.EntUser;

import java.util.UUID;

public interface IUserService {


    /**
     * Persists the seller object to the data store.
     * @param seller, {@link EntUser} model object to create
     */
    EntUser createSeller(EntUser seller);

    /**
     * Updates the seller object to the data store
     * @param seller, the {@link EntUser} model object to update
     */
    EntUser updateSeller(EntUser seller);

    /**
     * @param email, email id of the seller to fetch
     * @return, the {@link EntUser} object with email @param email.
     */
    EntUser findByEmail(String email);

    /**
     * @param id, id of the seller to fetch
     * @return, the {@link EntUser} object with id @param id.
     */
    EntUser findById(UUID id);

    /**
     * @param userName, user if for the new Seller not yet activated into the system
     * @param activationCode, a generated code sent to the Seller and used to activate the new Seller
     */
    void activateAccount(String userName, String activationCode);

    void resendActivationCode(String username);

    void register(EntUser seller);
}
