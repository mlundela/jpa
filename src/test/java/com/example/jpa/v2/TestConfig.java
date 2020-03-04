package com.example.jpa.v2;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@SpringBootApplication(scanBasePackages = "com.example.jpa.v2")
@EnableJpaRepositories
public class TestConfig {
}