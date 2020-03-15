package com.example.jpa.generics;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"parent"})
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Country {

    @Id private Long id;
    private String name;
    @OneToOne private Country parent;
    @OneToMany private Set<Country> children = new HashSet<>();

    public Country(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void deletePrecedence() {
        if (this.parent == null) {
            throw new RuntimeException("Participant has no precedence");
        }
        parent.getChildren().remove(this);
        parent = null;
    }

    public void setPrecedence(Country precedence) {
        this.parent = precedence;
        precedence.getChildren().add(this);
    }
}
