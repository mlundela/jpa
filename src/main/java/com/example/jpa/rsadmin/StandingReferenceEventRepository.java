package com.example.jpa.rsadmin;

import java.util.stream.Stream;

public interface StandingReferenceEventRepository extends RSRepository<StandingEvent> {
    Stream<StandingEvent> findByObject(EventEntity event);
}
