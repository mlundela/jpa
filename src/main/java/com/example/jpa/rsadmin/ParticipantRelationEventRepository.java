package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantRelationEventRepository extends JpaRepository<ParticipantRelationEvent, UUID> {
    Optional<ParticipantRelationEvent> findBySystemId(RSSystemId systemId);

    Stream<ParticipantRelationEvent> findByObject(EventEntity event);
}
