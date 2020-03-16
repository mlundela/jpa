package com.example.jpa.inheritance;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
//@AutoConfigureEmbeddedDatabase
@SpringBootApplication(scanBasePackages = "com.example.jpa.inheritance")
public class TestConfig {
}
