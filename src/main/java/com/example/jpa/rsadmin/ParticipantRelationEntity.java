package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.ParticipantRelationLoaded;
import no.tv2.sport.resultatservice.domain.model.ObjectType;
import no.tv2.sport.resultatservice.domain.model.ParticipantType;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "objectType")
@Table(name = "participant_relation")
public abstract class ParticipantRelationEntity extends RSEntity<ParticipantRelationEntity> {

    @Column(name = "`from`")
    private Instant from;

    @Column(name = "`to`")
    private Instant to;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private ParticipantType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "participantId", referencedColumnName = "id")
    private ParticipantEntity participant;

    public ParticipantRelationLoaded toParticipantRelationLoaded() {
        return ParticipantRelationLoaded.newBuilder()
                .setId(getId())
                .setFrom(getFrom())
                .setTo(getTo())
                .setActive(isActive())
                .setType(getType())
                .setParticipantId(participant != null ? participant.getPrecedenceId() : null)
                .setObjectType(getObjectType())
                .setObjectId(getObjectId())
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }

    protected abstract UUID getObjectId();

    protected abstract ObjectType getObjectType();
}
