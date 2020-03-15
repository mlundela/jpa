package com.example.jpa.generics;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MonkeyRepository extends JpaRepository<Monkey, Long> {
}
