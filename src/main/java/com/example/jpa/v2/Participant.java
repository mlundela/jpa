package com.example.jpa.v2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class Participant {

    @Id private Long id;
    private String name;
    @OneToOne private Participant parent;
    @OneToMany private Set<Participant> children = new HashSet<>();

    public Participant(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPrecedence(Participant precedence) {
        this.parent = precedence;
        precedence.getChildren().add(this);
    }
}
