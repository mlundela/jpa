package com.example.jpa.v3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired private ParticipantRepository repository;

    @Test
    public void test() throws Exception {
        final Participant p1 = repository.save(getParticipant("Henning", 1L, null));
        final Participant p2 = repository.save(getParticipant("Mads", 2L, p1));
        for (Participant participant : repository.findAll()) {
            System.out.println(participant);
        }
        final Participant p = repository.findById(1L).orElseThrow();
        assertThat(p.getChildren()).hasSize(1);
    }

    @Test
    public void test_delete() throws Exception {
        final Participant p1 = repository.save(getParticipant("Henning", 1L, null));
        final Participant p2 = repository.save(getParticipant("Mads", 2L, p1));

        p2.deletePrecedence();
        repository.saveAndFlush(p2);

        for (Participant participant : repository.findAll()) {
            System.out.println(participant);
        }
        final Participant p = repository.findById(1L).orElseThrow();
        assertThat(p.getChildren()).hasSize(0);
    }

    private Participant getParticipant(String name, long id, Participant precedence) {
        final Participant out = new Participant(id, name);
        if (precedence != null) {
            out.setPrecedence(precedence, "sjef");
        }
        return out;
    }
}