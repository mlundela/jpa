package com.example.jpa.v1;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Precedence {
    @Id private Long id;
    @OneToOne private Participant source;
    @OneToOne private Participant destination;
}
