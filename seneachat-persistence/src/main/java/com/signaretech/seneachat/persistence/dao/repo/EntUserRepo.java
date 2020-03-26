package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntUserRepo extends CrudRepository<EntUser, UUID> {

    EntUser findByUsername(String username);

    boolean existsByUsername(String username);
}
