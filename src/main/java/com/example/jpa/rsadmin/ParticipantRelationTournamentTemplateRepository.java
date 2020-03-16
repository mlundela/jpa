package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantRelationTournamentTemplateRepository extends JpaRepository<ParticipantRelationTournamentTemplate, UUID> {
    Optional<ParticipantRelationTournamentTemplate> findBySystemId(RSSystemId systemId);

    Stream<ParticipantRelationTournamentTemplate> findByObject(TournamentTemplateEntity TournamentTemplate);
}
