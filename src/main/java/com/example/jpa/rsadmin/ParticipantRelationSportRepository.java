package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantRelationSportRepository extends JpaRepository<ParticipantRelationSport, UUID> {
    Optional<ParticipantRelationSport> findBySystemId(RSSystemId systemId);

    Stream<ParticipantRelationSport> findByObject(SportEntity Sport);
}
