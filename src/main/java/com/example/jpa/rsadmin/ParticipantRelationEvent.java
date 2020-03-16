package com.example.jpa.rsadmin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.model.ObjectType;

import javax.persistence.*;
import java.util.UUID;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@DiscriminatorValue("event")
public class ParticipantRelationEvent extends ParticipantRelationEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "objectId", referencedColumnName = "id")
    private EventEntity object;

    @Override
    public ParticipantRelationEntity self() {
        return this;
    }

    @Override
    protected UUID getObjectId() {
        return object != null ? object.getPrecedenceId() : null;
    }

    @Override
    protected ObjectType getObjectType() {
        return ObjectType.EVENT;
    }
}

