package com.example.jpa.v3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Precedence implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) private Participant source;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) private Participant destination;

    private String type;

    public Precedence(Participant source, Participant destination, String type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    @Override
    public String toString() {
        return "PrecedenceV3{" +
                "id=" + id +
                ", source=" + source.getName() +
                ", destination=" + destination.getName() +
                ", type='" + type + '\'' +
                '}';
    }
}
