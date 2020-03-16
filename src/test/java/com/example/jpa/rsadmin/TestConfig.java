package com.example.jpa.rsadmin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@SpringBootApplication(scanBasePackages = {"com.example.jpa.rsadmin"})
@ComponentScan("com.example.jpa.rsadmin")
public class TestConfig {
}
