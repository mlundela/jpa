package com.example.jpa.rsadmin;

import no.tv2.sport.resultatservice.domain.model.ParticipantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface ParticipantRelationTournamentStageRepository extends JpaRepository<ParticipantRelationTournamentStage, UUID> {
    Stream<ParticipantRelationTournamentStage> findByObject(TournamentStageEntity TournamentStage);

    List<ParticipantRelationTournamentStage> findByTypeAndObjectId(ParticipantType type, UUID tournamentStageId);

    Optional<ParticipantRelationTournamentStage> findBySystemId(RSSystemId systemId);
}
