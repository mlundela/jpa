package com.example.jpa.rsadmin;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.*;
import no.tv2.sport.resultatservice.domain.model.LineupPosition;
import no.tv2.sport.resultatservice.domain.model.LineupType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@ToString(exclude = "eventParticipant")
@EqualsAndHashCode(exclude = "eventParticipant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class EventParticipantLineup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Integer shirtNumber;
    @Enumerated(EnumType.STRING)
    private LineupPosition position;
    @Enumerated(EnumType.STRING)
    private LineupType lineupType;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;
    @UpdateTimestamp
    private Instant updated;

    @Type(type = "hstore")
    @Column(columnDefinition = "hstore")
    private Map<String, String> properties;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "participantId", referencedColumnName = "id")
    private ParticipantEntity participant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private EventParticipantEntity eventParticipant;
}
