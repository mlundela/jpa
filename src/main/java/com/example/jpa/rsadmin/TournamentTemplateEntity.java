package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.TournamentTemplateLoaded;
import no.tv2.sport.resultatservice.domain.model.Gender;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "tournament_template")
public class TournamentTemplateEntity extends RSEntity<TournamentTemplateEntity> {

    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sportId", referencedColumnName = "id")
    private SportEntity sport;

    @Override
    public TournamentTemplateEntity self() {
        return this;
    }

    public TournamentTemplateLoaded toTournamentTemplateLoaded() {
        return TournamentTemplateLoaded.newBuilder()
                .setId(getId())
                .setName(getName())
                .setGender(getGender())
                .setSportId(sport != null ? sport.getPrecedenceId() : null)
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }
}

