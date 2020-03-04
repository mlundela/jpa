package com.example.jpa.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired private ParticipantRepository participantRepository;
    @Autowired private PrecedenceRepository precedenceRepository;

    @Test
    public void test() throws Exception {
        final Participant p1 = participantRepository.save(getParticipant("Henning", 1L));
        final Participant p2 = participantRepository.save(getParticipant("Mads", 2L));
        final Precedence pp = precedenceRepository.save(createPrecedence(p1, p2));
    }

    private Precedence createPrecedence(Participant p1, Participant p2) {
        final Precedence out = new Precedence();
        out.setId(1L);
        out.setSource(p1);
        out.setDestination(p2);
        return out;
    }

    private Participant getParticipant(String name, long id) {
        final Participant out = new Participant();
        out.setId(id);
        out.setName(name);
        return out;
    }

}