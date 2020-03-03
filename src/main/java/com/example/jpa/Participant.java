package com.example.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Participant {
    @Id private Long id;
    private String name;
}
