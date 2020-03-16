package com.example.jpa.rsadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.tv2.sport.resultatservice.domain.model.DataSystem;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class RSSystemId implements Serializable {
    @Enumerated(EnumType.STRING)
    @NotNull
    private DataSystem system;
    @NotNull
    private String systemId;

    public static RSSystemId of(DataSystem system, String systemId) {
        return new RSSystemId(system, systemId);
    }
}
