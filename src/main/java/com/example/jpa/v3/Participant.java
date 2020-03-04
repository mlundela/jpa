package com.example.jpa.v3;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Participant implements Serializable {

    @Id private Long id;

    @OneToOne(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
    private Precedence parent;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Precedence> children = new HashSet<>();

    private String name;

    public Participant(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPrecedence(Participant precedence, String name) {
        final Precedence link = new Precedence(this, precedence, name);
        this.parent = link;
        precedence.getChildren().add(link);
    }
}
