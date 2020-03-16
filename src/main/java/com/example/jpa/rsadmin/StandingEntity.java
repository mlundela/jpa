package com.example.jpa.rsadmin;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "object")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "standing")
public abstract class StandingEntity extends RSEntity<StandingEntity> {

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private StandingType standingType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<StandingConfig> config;

    @OneToMany(mappedBy = "standing", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StandingParticipantEntity> participants;

    @Override
    public StandingEntity self() {
        return this;
    }

//    public StandingLoaded toStandingLoaded() {
//        return StandingLoaded
//                .newBuilder()
//                .setId(getId())
//                .setParticipants(toStandingParticipantsLoaded(getParticipants()))
//                .setConfig(toStandingConfigsLoaded(getConfig()))
//                .setStandingType(toStandingTypeLoaded(getStandingType()))
//                .setObject(getObject())
//                .setObjectId(getObjectId())
//                .setProperties(getMergedProperties())
//                .setDeleted(isIgnored())
//                .build();
//    }

    protected abstract UUID getObjectId();

    protected abstract String getObject();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StandingType {
        private String name;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StandingConfig {
        private String value;
        private String subParam;
        private StandingTypeParam standingTypeParam;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StandingTypeParam {
        private String name;
        private String code;
        private String value;
        private String type;
    }
}
