package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StandingReferenceTournamentTemplateRepository extends JpaRepository<StandingTournamentTemplate, UUID> {
    Optional<StandingTournamentTemplate> findBySystemId(RSSystemId systemId);

    Stream<StandingTournamentTemplate> findByObject(TournamentTemplateEntity tournamentTemplate);
}
