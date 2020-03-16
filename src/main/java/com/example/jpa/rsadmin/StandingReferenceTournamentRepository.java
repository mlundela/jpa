package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StandingReferenceTournamentRepository extends JpaRepository<StandingTournament, UUID> {
    Optional<StandingTournament> findBySystemId(RSSystemId systemId);

    Stream<StandingTournament> findByObject(TournamentEntity tournament);
}
