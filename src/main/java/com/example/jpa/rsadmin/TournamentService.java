package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadTournament;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TournamentService extends RSService<RSCommandWrapper<LoadTournament>, TournamentEntity> {

    private final TournamentRepository tournamentRepository;
    private final TournamentTemplateService tournamentTemplateService;

    public TournamentService(TournamentRepository tournamentRepository, TournamentTemplateService tournamentTemplateService, ApplicationEventPublisher publisher) {
        super(tournamentRepository, publisher);
        this.tournamentRepository = tournamentRepository;
        this.tournamentTemplateService = tournamentTemplateService;
    }

    @Override
    public TournamentEntity createFromCommand(RSCommandWrapper<LoadTournament> wrapper) {
        final LoadTournament c = wrapper.getCommand();
        return TournamentEntity.builder().systemId(RSSystemId.of(c.getSystem(), c.getId()))
                .tournamentTemplate(tournamentTemplateService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(c.getSystem(), c.getTournamentTemplateId())))
                .name(c.getName())
                .properties(c.getProperties())
                .deleted(c.getDeleted())
                .build();
    }

    @Override
    public TournamentEntity createTemplate(RSSystemId systemId) {
        return TournamentEntity.builder()
                .systemId(systemId)
                .name("")
                .properties(Map.of())
                .build();
    }

    @EventListener
    public void onTournamentTemplatePrecedence(PrecedenceLoaded<TournamentTemplateEntity> cmd) {
        tournamentRepository
                .findByTournamentTemplate(cmd.getSource())
                .forEach(this::publish);
    }
}
