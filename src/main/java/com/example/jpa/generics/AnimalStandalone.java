package com.example.jpa.generics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "animal_type")
@Table(name = "animal")
public abstract class AnimalStandalone {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private AnimalStandalone parent;

    @OneToMany(mappedBy = "parent")
    private Set<AnimalStandalone> children = new HashSet<>();

    private String name;

    public void deletePrecedence() {
        if (this.parent == null) {
            throw new RuntimeException("Participant has no precedence");
        }
        parent.getChildren().remove(this);
        parent = null;
    }

    public abstract String getType();

    public void setPrecedence(AnimalStandalone precedence) {
        this.parent = precedence;
        precedence.getChildren().add(this);
    }

    @Override
    public String toString() {
        return String.format("%s.%d.%s => %s", getType(), getId(), getName(), getParent());
    }
}
