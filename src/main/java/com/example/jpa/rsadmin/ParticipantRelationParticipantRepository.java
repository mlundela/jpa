package com.example.jpa.rsadmin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantRelationParticipantRepository extends JpaRepository<ParticipantRelationParticipant, UUID> {
    Stream<ParticipantRelationParticipant> findByObject(ParticipantEntity participant);

    Page<ParticipantRelationParticipant> findByObjectIdAndActive(UUID id, boolean active, Pageable pageable);

    Optional<ParticipantRelationParticipant> findBySystemId(RSSystemId systemId);

}
