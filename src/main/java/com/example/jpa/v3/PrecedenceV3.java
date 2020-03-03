package com.example.jpa.v3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PrecedenceV3 implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) private ParticipantV3 source;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) private ParticipantV3 destination;

    private String type;

    public PrecedenceV3(ParticipantV3 source, ParticipantV3 destination, String type) {
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
