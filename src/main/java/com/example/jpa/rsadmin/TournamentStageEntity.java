package com.example.jpa.rsadmin;


import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.TournamentStageLoaded;
import no.tv2.sport.resultatservice.domain.model.Gender;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "tournament_stage")
public class TournamentStageEntity extends RSEntity<TournamentStageEntity> {

    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Instant startDate;
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tournamentId", referencedColumnName = "id")
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    private CountryEntity country;

    @Override
    public TournamentStageEntity self() {
        return this;
    }

    public TournamentStageLoaded toTournamentStageLoaded() {
        return TournamentStageLoaded.newBuilder()
                .setId(getId())
                .setName(getName())
                .setGender(getGender())
                .setStartDate(getStartDate())
                .setEndDate(getEndDate())
                .setTournamentId(tournament != null ? tournament.getPrecedenceId() : null)
                .setCountryId(country != null ? country.getPrecedenceId() : null)
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }
}
