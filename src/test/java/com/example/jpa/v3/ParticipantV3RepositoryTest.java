package com.example.jpa.v3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParticipantV3RepositoryTest {

    @Autowired private ParticipantV3Repository participantRepository;

    @Test
    public void test() throws Exception {
        final ParticipantV3 p1 = participantRepository.save(getParticipant("Henning", 1L, null));
        final ParticipantV3 p2 = participantRepository.save(getParticipant("Mads", 2L, p1));
        for (ParticipantV3 participant : participantRepository.findAll()) {
            System.out.println(participant);
        }
        final ParticipantV3 p = participantRepository.findById(1L).orElseThrow();
        assertThat(p.getChildren()).hasSize(1);
    }


    private ParticipantV3 getParticipant(String name, long id, ParticipantV3 precedence) {
        final ParticipantV3 out = new ParticipantV3(id, name);
        if (precedence != null) {
            out.setPrecedence(precedence, "sjef");
        }
        return out;
    }
}