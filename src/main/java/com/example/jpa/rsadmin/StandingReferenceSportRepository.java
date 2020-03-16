package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface StandingReferenceSportRepository extends JpaRepository<StandingSport, UUID> {
    Optional<StandingSport> findBySystemId(RSSystemId systemId);

    Stream<StandingSport> findByObject(SportEntity sport);
}
