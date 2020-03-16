package com.example.jpa.rsadmin;


import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import no.tv2.sport.resultatservice.domain.command.LoadStanding;
import no.tv2.sport.resultatservice.domain.command.LoadStandingParticipant;
import no.tv2.sport.resultatservice.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Map;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
class StandingServiceTest {

    @Autowired private StandingService service;

    @Test
    public void test_update_standing_with_different_object() throws Exception {
        service.createOrUpdate(List.of(
                createLoadStandingCommand("participant"),
                createLoadStandingCommand("event"),
                createLoadStandingCommand("sport"),
                createLoadStandingCommand("tournament"),
                createLoadStandingCommand("tournament_stage"),
                createLoadStandingCommand("tournament_template")
        ));
    }

    private RSCommandWrapper<LoadStanding> createLoadStandingCommand(String object) {
        LoadStanding c = new LoadStanding(
                DataSystem.ENETPULSE, "standing-1",
                object, "object-123",
                new StandingType("place", "The place of the participant"),
                List.of(new LoadStandingParticipant(
                        "1",
                        2L,
                        List.of(
                                new StandingData(
                                        "goals",
                                        "10",
                                        "goals scored"
                                ),
                                new StandingData(
                                        "yellow cards",
                                        "3",
                                        "yellow cards this season"
                                )
                        ),
                        Map.of()
                )),
                List.of(new StandingConfig(
                        "a",
                        "b",
                        new StandingTypeParam(
                                "standingtypeparam",
                                "xsa32",
                                "2",
                                "standingparam"
                        )
                )),
                false,
                Map.of()
        );
        return new RSCommandWrapper<>(c, RSSystemId.of(c.getSystem(), c.getId()));
    }


}
