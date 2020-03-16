package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipantEntity, UUID> {
    Optional<EventParticipantEntity> findByEventAndParticipant(EventEntity event, ParticipantEntity participant);

    Stream<EventParticipantEntity> findByParticipant(ParticipantEntity participant);
}

