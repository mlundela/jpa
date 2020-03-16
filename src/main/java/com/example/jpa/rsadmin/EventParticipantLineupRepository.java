package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface EventParticipantLineupRepository extends JpaRepository<EventParticipantLineup, UUID> {
    Optional<EventParticipantLineup> findByEventParticipantAndParticipant(EventParticipantEntity eventParticipant, ParticipantEntity participant);

    Stream<EventParticipantLineup> findByParticipant(ParticipantEntity participant);
}
