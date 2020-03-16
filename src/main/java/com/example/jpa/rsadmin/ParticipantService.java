package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadParticipant;
import no.tv2.sport.resultatservice.domain.model.Gender;
import no.tv2.sport.resultatservice.domain.model.ParticipantType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class ParticipantService extends RSService<RSCommandWrapper<LoadParticipant>, ParticipantEntity> {

    private final ParticipantRepository participantRepository;
    private final CountryService countryService;

    public ParticipantService(ParticipantRepository participantRepository, CountryService countryService, ApplicationEventPublisher publisher) {
        super(participantRepository, publisher);
        this.participantRepository = participantRepository;
        this.countryService = countryService;
    }

    @Override
    public ParticipantEntity createFromCommand(RSCommandWrapper<LoadParticipant> wrapper) {
        final LoadParticipant lp = wrapper.getCommand();
        return ParticipantEntity.builder()
                .systemId(wrapper.getSystemId())
                .gender(lp.getGender())
                .country(countryService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getCountryId())))
                .name(lp.getName())
                .properties(lp.getProperties())
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }

    @Override
    public ParticipantEntity createTemplate(RSSystemId systemId) {
        return ParticipantEntity.builder()
                .systemId(systemId)
                .name("")
                .gender(Gender.UNDEFINED)
                .type(ParticipantType.UNDEFINED)
                .properties(Map.of())
                .build();
    }

    @EventListener
    public void on(PrecedenceLoaded<CountryEntity> cmd) {
        participantRepository.findByCountry(cmd.getSource()).forEach(this::publish);
    }
}
