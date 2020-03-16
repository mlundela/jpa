package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ParticipantRelationRepository extends RSRepository<ParticipantRelationEntity> {
    Stream<ParticipantRelationEntity> findByParticipant(ParticipantEntity participant);
}
