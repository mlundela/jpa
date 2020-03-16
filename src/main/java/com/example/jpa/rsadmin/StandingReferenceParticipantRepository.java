package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StandingReferenceParticipantRepository extends JpaRepository<StandingReferenceParticipant, UUID> {
    Optional<StandingReferenceParticipant> findBySystemId(RSSystemId systemId);

    Stream<StandingReferenceParticipant> findByObject(ParticipantEntity participant);
}
