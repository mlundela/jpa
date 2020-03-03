package com.example.jpa.v1;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class ParticipantV1Precedence {
    @Id private Long id;
    @OneToOne private ParticipantV1 source;
    @OneToOne private ParticipantV1 destination;
}
