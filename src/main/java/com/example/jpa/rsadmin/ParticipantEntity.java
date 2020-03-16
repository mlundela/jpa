package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.ParticipantLoaded;
import no.tv2.sport.resultatservice.domain.model.Gender;
import no.tv2.sport.resultatservice.domain.model.ParticipantType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "participant")
public class ParticipantEntity extends RSEntity<ParticipantEntity> {

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ParticipantType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    private CountryEntity country;

    @Override
    public ParticipantEntity self() {
        return this;
    }

    public ParticipantLoaded toEvent() {
        return ParticipantLoaded.newBuilder()
                .setId(getId())
                .setName(getName())
                .setGender(getGender())
                .setType(getType())
                .setCountryId(country != null ? country.getPrecedenceId() : null)
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }
}
