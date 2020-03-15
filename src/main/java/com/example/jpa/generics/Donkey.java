package com.example.jpa.generics;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@DiscriminatorValue("donkey")
public class Donkey extends AnimalStandalone {
    @Override
    public String getType() {
        return "DONKEY";
    }
}
