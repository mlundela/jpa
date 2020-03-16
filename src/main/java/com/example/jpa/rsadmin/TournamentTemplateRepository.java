package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface TournamentTemplateRepository extends RSRepository<TournamentTemplateEntity> {
    Stream<TournamentTemplateEntity> findBySport(SportEntity sport);

    List<TournamentTemplateEntity> findBySportIdOrderByName(UUID sportId);
}
