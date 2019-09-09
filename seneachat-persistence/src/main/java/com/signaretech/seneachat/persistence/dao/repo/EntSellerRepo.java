package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntSeller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntSellerRepo extends CrudRepository<EntSeller, UUID> {

    EntSeller findByEmail(String email);

    boolean existsByEmail(String email);
}
