package com.example.jpa.rsadmin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.UUID;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@DiscriminatorValue("sport")
public class StandingSport extends StandingEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "objectId", referencedColumnName = "id")
    private SportEntity object;

    @Override
    protected UUID getObjectId() {
        return object != null ? object.getPrecedenceId() : null;
    }

    @Override
    protected String getObject() {
        return "sport";
    }
}
