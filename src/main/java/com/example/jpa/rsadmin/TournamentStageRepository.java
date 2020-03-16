package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface TournamentStageRepository extends RSRepository<TournamentStageEntity> {
    Stream<TournamentStageEntity> findByCountry(CountryEntity country);

    Stream<TournamentStageEntity> findByTournament(TournamentEntity tournament);

    List<TournamentStageEntity> findByTournamentIdOrderByName(UUID tournamentId);
}
