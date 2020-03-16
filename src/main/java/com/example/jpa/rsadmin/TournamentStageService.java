package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadTournamentStage;
import no.tv2.sport.resultatservice.domain.model.Gender;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class TournamentStageService extends RSService<RSCommandWrapper<LoadTournamentStage>, TournamentStageEntity> {

    private final TournamentStageRepository tournamentStageRepository;
    private final CountryService countryService;
    private final TournamentService tournamentService;

    public TournamentStageService(TournamentStageRepository tournamentStageRepository, CountryService countryService, TournamentService tournamentService, ApplicationEventPublisher publisher) {
        super(tournamentStageRepository, publisher);
        this.tournamentStageRepository = tournamentStageRepository;
        this.countryService = countryService;
        this.tournamentService = tournamentService;
    }

    @Override
    public TournamentStageEntity createFromCommand(RSCommandWrapper<LoadTournamentStage> wrapper) {
        final LoadTournamentStage c = wrapper.getCommand();
        return TournamentStageEntity.builder().systemId(RSSystemId.of(c.getSystem(), c.getId()))
                .gender(c.getGender())
                .country(countryService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(c.getSystem(), c.getCountryId())))
                .tournament(tournamentService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(c.getSystem(), c.getTournamentId())))
                .name(c.getName())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .properties(c.getProperties())
                .deleted(c.getDeleted())
                .build();
    }

    @Override
    public TournamentStageEntity createTemplate(RSSystemId systemId) {
        return TournamentStageEntity.builder()
                .systemId(systemId)
                .name("")
                .properties(Map.of())
                .gender(Gender.UNDEFINED)
                .build();
    }


    @EventListener
    public void onCountryPrecedence(PrecedenceLoaded<CountryEntity> cmd) {
        tournamentStageRepository
                .findByCountry(cmd.getSource())
                .forEach(this::publish);
    }

    @EventListener
    public void onTournamentPrecedence(PrecedenceLoaded<TournamentEntity> cmd) {
        tournamentStageRepository
                .findByTournament(cmd.getSource())
                .forEach(this::publish);
    }
}
