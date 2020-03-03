package com.example.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void test() throws Exception {
        participantRepository.save(getParticipant());
    }

    private Participant getParticipant() {
        final Participant out = new Participant();
        out.setId(1L);
        out.setName("Mads");
        return out;
    }

}