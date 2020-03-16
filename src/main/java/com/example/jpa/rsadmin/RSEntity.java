package com.example.jpa.rsadmin;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"children"})
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public abstract class RSEntity<Entity extends RSEntity<Entity>> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Embedded
    @Valid
    private RSSystemId systemId;
    private boolean deleted;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;
    @UpdateTimestamp
    private Instant updated;

    @Type(type = "hstore")
    @Column(columnDefinition = "hstore")
    private Map<String, String> properties;

    @OneToOne
    private Entity parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private Set<Entity> children = new HashSet<>();

    public void deletePrecedence() {
        if (getParent() == null) {
            throw new RuntimeException("Participant has no precedence");
        }
        getParent().getChildrenSafe().remove(self());
        setParent(null);
    }

    public Set<Entity> getChildrenSafe() {
        if (getChildren() == null) {
            setChildren(new HashSet<>());
        }
        return getChildren();
    }

    public Map<String, String> getMergedProperties() {
        final Map<String, String> out = new HashMap<>(getProperties());
        for (Entity source : getChildrenSafe()) {
            final String system = source.getSystemId().getSystem().toString().toLowerCase();
            for (Map.Entry<String, String> entry : source.getProperties().entrySet()) {
                out.put(String.format("%s:%s", system, entry.getKey()), entry.getValue());
            }
        }
        return out;
    }

    public UUID getPrecedenceId() {
        return Optional.ofNullable(getParent())
                .map(Entity::getPrecedenceId)
                .orElseGet(this::getId);
    }

    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    public boolean isIgnored() {
        return isDeleted() || getParent() != null;
    }

    public abstract Entity self();

    public void setPrecedence(Entity precedence) {
        if (getParent() != null) {
            getParent().getChildrenSafe().remove(self());
        }
        setParent(precedence);
        precedence.getChildrenSafe().add(self());
    }
}
