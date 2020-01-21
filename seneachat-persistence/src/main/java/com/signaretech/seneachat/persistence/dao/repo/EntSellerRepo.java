package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntSeller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntSellerRepo extends CrudRepository<EntSeller, UUID> {

    EntSeller findByUsername(String username);

    boolean existsByUsername(String username);
}
