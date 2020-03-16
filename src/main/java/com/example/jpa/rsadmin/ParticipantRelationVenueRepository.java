package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantRelationVenueRepository extends JpaRepository<ParticipantRelationVenue, UUID> {
    Optional<ParticipantRelationVenue> findBySystemId(RSSystemId systemId);

    Stream<ParticipantRelationVenue> findByObject(VenueEntity venue);
}
