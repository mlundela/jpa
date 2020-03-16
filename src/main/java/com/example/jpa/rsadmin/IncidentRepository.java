package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface IncidentRepository extends RSRepository<IncidentEntity> {
    Stream<IncidentEntity> findByEvent(EventEntity event);

    Stream<IncidentEntity> findByParticipant(ParticipantEntity participant);

    Stream<IncidentEntity> findByReferencedParticipant(ParticipantEntity participant);
}
