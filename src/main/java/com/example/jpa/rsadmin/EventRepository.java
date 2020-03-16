package com.example.jpa.rsadmin;

import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface EventRepository extends RSRepository<EventEntity> {

    Stream<EventEntity> findByTournamentStage(TournamentStageEntity tournamentStage);

    Stream<EventEntity> findByVenue(VenueEntity venue);
}
