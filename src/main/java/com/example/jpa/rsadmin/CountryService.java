package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadCountry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class CountryService extends RSService<RSCommandWrapper<LoadCountry>, CountryEntity> {

    public CountryService(RSRepository<CountryEntity> repository, ApplicationEventPublisher publisher) {
        super(repository, publisher);
    }

    @Override
    public CountryEntity createFromCommand(RSCommandWrapper<LoadCountry> wrapper) {
        final LoadCountry c = wrapper.getCommand();
        return CountryEntity.builder()
                .systemId(wrapper.getSystemId())
                .name(c.getName())
                .properties(c.getProperties())
                .deleted(c.getDeleted())
                .build();
    }

    @Override
    public CountryEntity createTemplate(RSSystemId systemId) {
        return CountryEntity.builder()
                .systemId(systemId)
                .name("")
                .properties(Map.of())
                .build();
    }
}
