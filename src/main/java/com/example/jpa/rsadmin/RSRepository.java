package com.example.jpa.rsadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface RSRepository<Entity> extends JpaRepository<Entity, UUID> {
    Optional<Entity> findBySystemId(RSSystemId systemId);
}
