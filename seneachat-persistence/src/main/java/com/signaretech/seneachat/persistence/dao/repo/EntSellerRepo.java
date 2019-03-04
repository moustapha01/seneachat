package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntSeller;

import java.util.UUID;

public interface EntSellerRepo {

    EntSeller findEntSellerByEmail(String email);

    EntSeller findBySellerId(UUID id);

    void create(EntSeller seller);

    void update(EntSeller seller);
}
