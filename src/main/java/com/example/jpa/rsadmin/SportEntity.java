package com.example.jpa.rsadmin;


import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.SportLoaded;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "sport")
public class SportEntity extends RSEntity<SportEntity> {

    private String name;

    @Override
    public SportEntity self() {
        return this;
    }

    public SportLoaded toSportLoaded() {
        return SportLoaded.newBuilder()
                .setId(getId())
                .setName(getName())
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }
}
