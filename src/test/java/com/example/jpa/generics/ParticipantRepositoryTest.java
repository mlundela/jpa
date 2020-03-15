package com.example.jpa.generics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired private ParticipantRepository repository;

    @Test
    public void create() throws Exception {
        final Participant p1 = repository.save(getParticipant("Henning", 1L, null));
        final Participant p2 = repository.save(getParticipant("Mads", 2L, p1));
        for (Participant participant : repository.findAll()) {
            System.out.println(participant);
        }
        final Participant p = repository.findById(1L).orElseThrow();
        assertThat(p.getChildren()).hasSize(1);
    }

    @Test
    public void delete() throws Exception {
        final Participant p1 = repository.save(getParticipant("Henning", 1L, null));
        final Participant p2 = repository.save(getParticipant("Mads", 2L, p1));

        p2.deletePrecedence();
        repository.save(p2);

        for (Participant participant : repository.findAll()) {
            System.out.println(participant);
        }
        final Participant p = repository.findById(1L).orElseThrow();
        assertThat(p.getChildren()).hasSize(0);
    }

    private Participant getParticipant(String name, long id, Participant precedence) {
        final Participant out = new Participant();
        out.setId(id);
        out.setName(name);
        if (precedence != null) {
            out.setPrecedence(precedence);
        }
        return out;
    }
}