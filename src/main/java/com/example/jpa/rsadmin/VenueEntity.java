package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.VenueLoaded;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "venue")
public class VenueEntity extends RSEntity<VenueEntity> {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    private CountryEntity country;

    @Override
    public VenueEntity self() {
        return this;
    }

    public VenueLoaded toVenueLoaded() {
        return VenueLoaded.newBuilder()
                .setId(getId())
                .setName(getName())
                .setCountryId(country != null ? country.getPrecedenceId() : null)
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }
}

