package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StandingParticipantRepository extends JpaRepository<StandingParticipantEntity, UUID> {
    Optional<StandingParticipantEntity> findByStandingAndParticipant(StandingEntity standing, ParticipantEntity participant);

    Stream<StandingParticipantEntity> findByParticipant(ParticipantEntity participant);
}
