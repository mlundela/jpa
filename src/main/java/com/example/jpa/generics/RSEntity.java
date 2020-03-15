package com.example.jpa.generics;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Getter
//@Setter
//@EqualsAndHashCode(of = "id")


@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class RSEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private RSEntity parent;

    @OneToMany(mappedBy = "parent")
    private Set<RSEntity> children = new HashSet<>();

    public void deletePrecedence() {
        if (this.parent == null) {
            throw new RuntimeException("Participant has no precedence");
        }
        parent.getChildren().remove(this);
        parent = null;
    }

    public void setPrecedence(RSEntity precedence) {
        this.parent = precedence;
        precedence.getChildren().add(this);
    }
}
