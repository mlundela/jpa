package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.IncidentLoaded;
import no.tv2.sport.resultatservice.domain.model.IncidentType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "incident")
public class IncidentEntity extends RSEntity<IncidentEntity> {

    @Enumerated(EnumType.STRING)
    private IncidentType incidentType;
    private Integer elapsedTime;
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "eventId", referencedColumnName = "id")
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "participantId", referencedColumnName = "id")
    private ParticipantEntity participant;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "referencedParticipantId", referencedColumnName = "id")
    private ParticipantEntity referencedParticipant;

    @Override
    public IncidentEntity self() {
        return this;
    }

    public IncidentLoaded toEvent() {
        return IncidentLoaded.newBuilder()
                .setId(getId())
                .setIncidentType(getIncidentType())
                .setElapsedTime(getElapsedTime())
                .setSortOrder(getSortOrder())
                .setEventId(event != null ? event.getPrecedenceId() : null)
                .setParticipantId(participant != null ? participant.getPrecedenceId() : null)
                .setReferencedParticipantId(referencedParticipant != null ? referencedParticipant.getPrecedenceId() : null)
                .setProperties(getMergedProperties())
                .setDeleted(isIgnored())
                .build();
    }
}
