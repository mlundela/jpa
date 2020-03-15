package com.example.jpa.inheritance;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
abstract class RSEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
}
