package com.example.jpa.generics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MonkeyRepositoryTest {

    @Autowired private MonkeyRepository repository;

    @Test
    public void monkey() throws Exception {
        final Monkey m1 = new Monkey();
        m1.setName("Julius");
        repository.save(m1);

        final Monkey m2 = new Monkey();
        m2.setName("Frank");
        m2.setParent(m1);
        repository.save(m2);

        final List<Monkey> all = repository.findAll();

        for (Monkey monkey : all) {
            System.out.println(monkey);
        }

        assertThat(all).hasSize(2);
    }
}