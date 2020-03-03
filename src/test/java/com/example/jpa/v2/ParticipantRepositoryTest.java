package com.example.jpa.v2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired private ParticipantV2Repository participantRepository;

    @Test
    public void test() throws Exception {
        final ParticipantV2 p1 = participantRepository.save(getParticipant("Henning", 1L, null));
        final ParticipantV2 p2 = participantRepository.save(getParticipant("Mads", 2L, p1));
        for (ParticipantV2 participant : participantRepository.findAll()) {
            System.out.println(participant);
        }
        final ParticipantV2 p = participantRepository.findById(1L).orElseThrow();
        assertThat(p.getChildren()).hasSize(1);
    }


    private ParticipantV2 getParticipant(String name, long id, ParticipantV2 precedence) {
        final ParticipantV2 out = new ParticipantV2(id, name);
        if (precedence != null) {
            out.setPrecedence(precedence);
        }
        return out;
    }

}