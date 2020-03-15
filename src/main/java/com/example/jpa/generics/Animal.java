package com.example.jpa.generics;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

//@Entity
//@Getter
//@Setter
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "animal_type")
//@Table(name = "animal")



@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "objectType")
public class Animal extends RSEntity {

    private String name;

    @Override
    public String toString() {
        return String.format("%s.%d.%s => %s", "animal", getId(), getName(), getParent());
    }
}
