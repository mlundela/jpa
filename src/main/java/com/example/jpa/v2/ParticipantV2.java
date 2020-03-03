package com.example.jpa.v2;

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
//@Data
@ToString(exclude = {"parent"})
@NoArgsConstructor
public class ParticipantV2 {

    @Id private Long id;
    private String name;
    @OneToOne private ParticipantV2 parent;
    @OneToMany private Set<ParticipantV2> children = new HashSet<>();

    public ParticipantV2(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPrecedence(ParticipantV2 precedence) {
        this.parent = precedence;
        precedence.getChildren().add(this);
    }
}
