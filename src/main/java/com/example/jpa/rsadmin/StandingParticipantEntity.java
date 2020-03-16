package com.example.jpa.rsadmin;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ToString(exclude = "standing")
@EqualsAndHashCode(exclude = "standing")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "standing_participant")
@TypeDefs({
        @TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class StandingParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;
    @UpdateTimestamp
    private Instant updated;

    private Long rank;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<StandingData> data;

    @Type(type = "hstore")
    @Column(columnDefinition = "hstore")
    private Map<String, String> properties;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "participantId", referencedColumnName = "id")
    private ParticipantEntity participant;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private StandingEntity standing;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StandingData {
        private String code;
        private String value;
        private String subParam;

    }

}
