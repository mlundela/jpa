package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StandingReferenceTournamentStageRepository extends JpaRepository<StandingTournamentStage, UUID> {
    Optional<StandingTournamentStage> findBySystemId(RSSystemId systemId);

    Stream<StandingTournamentStage> findByObject(TournamentStageEntity tournamentStage);
}
