package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadSport;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SportService extends RSService<RSCommandWrapper<LoadSport>, SportEntity> {

    public SportService(RSRepository<SportEntity> repository, ApplicationEventPublisher publisher) {
        super(repository, publisher);
    }

    @Override
    public SportEntity createFromCommand(RSCommandWrapper<LoadSport> wrapper) {
        final LoadSport c = wrapper.getCommand();
        final RSSystemId systemId = wrapper.getSystemId();
        return SportEntity.builder()
                .systemId(systemId)
                .name(c.getName())
                .deleted(c.getDeleted())
                .properties(c.getProperties())
                .build();
    }

    @Override
    public SportEntity createTemplate(RSSystemId systemId) {
        return SportEntity.builder()
                .systemId(systemId)
                .name("")
                .properties(Map.of())
                .build();
    }
}
