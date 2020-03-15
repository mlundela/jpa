package com.example.jpa.inheritance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MonkeyRepository extends JpaRepository<Monkey, Long> {
}
