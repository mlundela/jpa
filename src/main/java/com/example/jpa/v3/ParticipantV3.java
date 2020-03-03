package com.example.jpa.v3;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class ParticipantV3 implements Serializable {

    @Id private Long id;

    @OneToOne(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
    private PrecedenceV3 parent;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PrecedenceV3> children = new HashSet<>();

    private String name;

    public ParticipantV3(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPrecedence(ParticipantV3 precedence, String name) {
        final PrecedenceV3 link = new PrecedenceV3(this, precedence, name);
        this.parent = link;
        precedence.getChildren().add(link);
    }
}
