package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;
import no.tv2.sport.resultatservice.domain.event.TournamentLoaded;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "tournament")
public class TournamentEntity extends RSEntity<TournamentEntity> {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tournamentTemplateId", referencedColumnName = "id")
    private TournamentTemplateEntity tournamentTemplate;

    @Override
    public TournamentEntity self() {
        return this;
    }

    public TournamentLoaded toTournamentLoaded() {
        return TournamentLoaded.newBuilder()
                .setId(getId())
                .setName(getName())
                .setTournamentTemplateId(tournamentTemplate != null ? tournamentTemplate.getPrecedenceId() : null)
                .setDeleted(isIgnored())
                .setProperties(getMergedProperties())
                .build();
    }
}
