package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.model.EventStatus;
import no.tv2.sport.resultatservice.domain.model.EventStatusDescription;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "event")
public class EventEntity extends RSEntity<EventEntity> {

    private String name;

    private Instant startDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Enumerated(EnumType.STRING)
    private EventStatusDescription statusDescription;

    private boolean venueNeutralGround;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tournamentStageId", referencedColumnName = "id")
    private TournamentStageEntity tournamentStage;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "venueId", referencedColumnName = "id")
    private VenueEntity venue;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventParticipantEntity> participants;

    @Override
    public EventEntity self() {
        return this;
    }
}
