package com.example.jpa.rsadmin;

import no.tv2.sport.resultatservice.domain.model.DataSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportRepository extends RSRepository<SportEntity> {
    List<SportEntity> findBySystemIdSystemOrderByName(DataSystem system);
}

