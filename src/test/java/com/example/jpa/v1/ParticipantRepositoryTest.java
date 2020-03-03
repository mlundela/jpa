package com.example.jpa.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired private ParticipantV1Repository participantRepository;
    @Autowired private PrecedenceV1Repository precedenceRepository;

    @Test
    public void test() throws Exception {
        final ParticipantV1 p1 = participantRepository.save(getParticipant("Henning", 1L));
        final ParticipantV1 p2 = participantRepository.save(getParticipant("Mads", 2L));
        final PrecedenceV1 pp = precedenceRepository.save(createPrecedence(p1, p2));
    }

    private PrecedenceV1 createPrecedence(ParticipantV1 p1, ParticipantV1 p2) {
        final PrecedenceV1 out = new PrecedenceV1();
        out.setId(1L);
        out.setSource(p1);
        out.setDestination(p2);
        return out;
    }

    private ParticipantV1 getParticipant(String name, long id) {
        final ParticipantV1 out = new ParticipantV1();
        out.setId(id);
        out.setName(name);
        return out;
    }

}