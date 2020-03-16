package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.stream.Stream;


@Repository
public interface VenueRepository extends RSRepository<VenueEntity> {
    Stream<VenueEntity> findByCountry(CountryEntity country);
}
