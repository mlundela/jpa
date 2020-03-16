package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadTournamentTemplate;
import no.tv2.sport.resultatservice.domain.model.Gender;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TournamentTemplateService extends RSService<RSCommandWrapper<LoadTournamentTemplate>, TournamentTemplateEntity> {

    private final TournamentTemplateRepository tournamentTemplateRepository;
    private final SportService sportService;

    public TournamentTemplateService(TournamentTemplateRepository tournamentTemplateRepository, SportService sportService, ApplicationEventPublisher publisher) {
        super(tournamentTemplateRepository, publisher);
        this.tournamentTemplateRepository = tournamentTemplateRepository;
        this.sportService = sportService;
    }

    @Override
    public TournamentTemplateEntity createFromCommand(RSCommandWrapper<LoadTournamentTemplate> wrapper) {
        final LoadTournamentTemplate c = wrapper.getCommand();
        return TournamentTemplateEntity.builder().systemId(RSSystemId.of(c.getSystem(), c.getId()))
                .gender(c.getGender())
                .sport(sportService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(c.getSystem(), c.getSportId())))
                .name(c.getName())
                .deleted(c.getDeleted())
                .properties(c.getProperties())
                .build();
    }

    @Override
    public TournamentTemplateEntity createTemplate(RSSystemId systemId) {
        return TournamentTemplateEntity.builder()
                .systemId(systemId)
                .name("")
                .gender(Gender.UNDEFINED)
                .properties(Map.of())
                .build();
    }

    @EventListener
    public void onSportPrecedence(PrecedenceLoaded<SportEntity> cmd) {
        tournamentTemplateRepository.findBySport(cmd.getSource()).forEach(this::publish);
    }
}

