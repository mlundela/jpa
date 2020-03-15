package com.example.jpa.inheritance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DonkeyRepository extends JpaRepository<Donkey, Long> {
}
