package com.example.jpa.generics;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<AnimalStandalone, Long> {
}
