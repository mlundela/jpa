package com.example.jpa.rsadmin;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import no.tv2.sport.resultatservice.domain.model.ResultType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@ToString(exclude = "event")
@EqualsAndHashCode(exclude = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "event_participant")
@TypeDefs({
        @TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class EventParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Integer number;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;
    @UpdateTimestamp
    private Instant updated;

    @OneToMany(mappedBy = "eventParticipant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventParticipantLineup> lineup;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "participantId", referencedColumnName = "id")
    private ParticipantEntity participant;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Set<EventParticipantResult> results;

    @Type(type = "hstore")
    @Column(columnDefinition = "hstore")
    private Map<String, String> properties;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private EventEntity event;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventParticipantResult {
        private String value;
        private ResultType resultType;

        public String getValue() {
            return value != null ? value : "";
        }
    }
}
