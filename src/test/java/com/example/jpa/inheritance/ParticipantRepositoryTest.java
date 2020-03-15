package com.example.jpa.inheritance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired private AnimalRepository animals;
    @Autowired private MonkeyRepository monkeys;
    @Autowired private DonkeyRepository donkeys;

    @Test
    public void test() throws Exception {

        final Monkey monkey = new Monkey();
        monkey.setName("Julius");
        monkey.setHasBanana(true);
        animals.save(monkey);

        final Donkey donkey = new Donkey();
        donkey.setName("Honky Donkey Blues");
        donkey.setAngry(true);
        animals.save(donkey);

        assertThat(monkey.getId()).isNotNull();
        assertThat(animals.count()).isEqualTo(2);
        assertThat(monkeys.count()).isEqualTo(1);
        assertThat(donkeys.count()).isEqualTo(1);

        assertThat(monkeys.findAll()).containsOnly(monkey);
        assertThat(donkeys.findAll()).containsOnly(donkey);
        assertThat(animals.findAll()).containsOnly(monkey, donkey);

        assertThat(animals.existsById(monkey.getId())).isTrue();
        assertThat(animals.existsById(donkey.getId())).isTrue();
        assertThat(monkeys.existsById(monkey.getId())).isTrue();
        assertThat(monkeys.existsById(donkey.getId())).isFalse();

        donkeys.deleteAll();
        assertThat(animals.count()).isEqualTo(1);
        assertThat(monkeys.count()).isEqualTo(1);
        assertThat(donkeys.count()).isEqualTo(0);

        animals.deleteAll();
        assertThat(animals.count()).isEqualTo(0);
        assertThat(monkeys.count()).isEqualTo(0);
    }
}
