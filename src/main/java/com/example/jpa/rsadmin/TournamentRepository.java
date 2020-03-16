package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface TournamentRepository extends RSRepository<TournamentEntity> {

    Stream<TournamentEntity> findByTournamentTemplate(TournamentTemplateEntity tournamentTemplate);

    List<TournamentEntity> findByTournamentTemplateIdOrderByNameDesc(UUID tournamentTemplateId);
}
