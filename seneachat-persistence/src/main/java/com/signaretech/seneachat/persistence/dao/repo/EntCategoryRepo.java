package com.signaretech.seneachat.persistence.dao.repo;

import com.signaretech.seneachat.persistence.entity.EntCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntCategoryRepo extends CrudRepository<EntCategory, UUID> {


    List<EntCategory> findByParentName(String name);

    List<EntCategory> findByParentNull();

    EntCategory findByName(String name);
}
