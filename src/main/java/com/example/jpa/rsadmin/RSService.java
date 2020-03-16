package com.example.jpa.rsadmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
public abstract class RSService<Command extends RSCommand, Entity extends RSEntity<Entity>> {

    private final RSRepository<Entity> repository;
    private final ApplicationEventPublisher publisher;

    public abstract Entity createFromCommand(Command cmd);

    @Transactional
    public void createOrUpdate(Collection<Command> commands) {
        for (Command command : commands) {
            final Entity entity = createOrUpdate(command);
            publish(entity);
        }
        System.out.println("DONE!");
    }

    @Transactional
    public void createPrecedence(UUID sourceId, UUID destinationId) {
        updatePrecedence(sourceId, destinationId, false);
    }

    public abstract Entity createTemplate(RSSystemId systemId);

    @Transactional
    public void deletePrecedence(UUID sourceId, UUID destinationId) {
        updatePrecedence(sourceId, destinationId, true);
    }

    public Entity findBySystemAndSystemIdOrCreateTemplate(RSSystemId systemId) {
        return repository
                .findBySystemId(systemId)
                .orElseGet(() -> {
                    final Entity entity = createTemplate(systemId);
                    log.info("Found no entity {} with {}. Creating new template", entity.getClass().getSimpleName(), systemId);
                    repository.save(entity);
                    publish(entity);
                    return entity;
                });
    }

    public void publish(Entity entity) {
        publisher.publishEvent(entity);
    }

    private Entity createOrUpdate(Command cmd) {
        final Entity entity = createFromCommand(cmd);
        final UUID id;
        try {
            id = entity.getId() != null ? entity.getId() : repository
                    .findBySystemId(cmd.getSystemId())
                    .map(RSEntity::getId)
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        entity.setId(id);
        return repository.save(entity);
    }

    private void updatePrecedence(UUID sourceId, UUID destinationId, boolean deleted) {
        final Entity source = repository.findById(sourceId).orElseThrow();
        final Entity destination = repository.findById(destinationId).orElseThrow();
        final PrecedenceLoaded<Entity> event = new PrecedenceLoaded<>(source, destination, deleted);
        if (deleted) {
            log.info("Delete precedence {}", event);
            source.deletePrecedence();
        } else {
            log.info("Create precedence {}", event);
            source.setPrecedence(destination);
        }
        repository.save(source);
        publisher.publishEvent(event);
        publish(source);
        publish(destination);
    }
}
