package com.example.jpa.rsadmin;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "country")
public class CountryEntity extends RSEntity<CountryEntity> {

    private String name;

    public String getShortCode() {
        if (name == null || name.isBlank()) {
            return "UNKNOWN";
        }
        return name.substring(0, 3).toUpperCase();
    }

    @Override
    public CountryEntity self() {
        return this;
    }

}
