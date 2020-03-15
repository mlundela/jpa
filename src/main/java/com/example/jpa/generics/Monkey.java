package com.example.jpa.generics;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@DiscriminatorValue("monkey")
public class Monkey extends AnimalStandalone {
    @Override
    public String getType() {
        return "MONKEY";
    }
}
