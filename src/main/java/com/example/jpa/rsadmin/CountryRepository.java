package com.example.jpa.rsadmin;

import no.tv2.sport.resultatservice.domain.model.DataSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends RSRepository<CountryEntity> {
    List<CountryEntity> findBySystemIdSystem(DataSystem system);
}
