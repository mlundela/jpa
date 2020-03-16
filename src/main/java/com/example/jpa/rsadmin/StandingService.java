package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadStanding;
import no.tv2.sport.resultatservice.domain.command.LoadStandingParticipant;
import no.tv2.sport.resultatservice.domain.model.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class StandingService extends RSService<RSCommandWrapper<LoadStanding>, StandingEntity> {

    private final StandingRepository standingRepository;
    private final EventService eventService;
    private final SportService sportService;
    private final TournamentService tournamentService;
    private final TournamentStageService tournamentStageService;
    private final TournamentTemplateService tournamentTemplateService;
    private final ParticipantService participantService;
    private final StandingParticipantRepository standingParticipantRepository;
    private final StandingReferenceParticipantRepository standingReferenceParticipantRepository;
    private final StandingReferenceEventRepository standingReferenceEventRepository;
    private final StandingReferenceSportRepository standingReferenceSportRepository;
    private final StandingReferenceTournamentRepository standingReferenceTournamentRepository;
    private final StandingReferenceTournamentStageRepository standingReferenceTournamentStageRepository;
    private final StandingReferenceTournamentTemplateRepository standingReferenceTournamentTemplateRepository;

    public StandingService(
            StandingRepository standingRepository,
            EventService eventService,
            SportService sportService,
            TournamentService tournamentService,
            TournamentStageService tournamentStageService,
            TournamentTemplateService tournamentTemplateService,
            ParticipantService participantService,
            StandingParticipantRepository standingParticipantRepository,
            StandingReferenceParticipantRepository standingReferenceParticipantRepository,
            StandingReferenceEventRepository standingReferenceEventRepository,
            StandingReferenceSportRepository standingReferenceSportRepository,
            StandingReferenceTournamentRepository standingReferenceTournamentRepository,
            StandingReferenceTournamentStageRepository standingReferenceTournamentStageRepository,
            StandingReferenceTournamentTemplateRepository standingReferenceTournamentTemplateRepository,
            ApplicationEventPublisher publisher) {

        super(standingRepository, publisher);
        this.standingRepository = standingRepository;
        this.eventService = eventService;
        this.sportService = sportService;
        this.tournamentService = tournamentService;
        this.tournamentStageService = tournamentStageService;
        this.tournamentTemplateService = tournamentTemplateService;
        this.participantService = participantService;
        this.standingParticipantRepository = standingParticipantRepository;
        this.standingReferenceParticipantRepository = standingReferenceParticipantRepository;
        this.standingReferenceEventRepository = standingReferenceEventRepository;
        this.standingReferenceSportRepository = standingReferenceSportRepository;
        this.standingReferenceTournamentRepository = standingReferenceTournamentRepository;
        this.standingReferenceTournamentStageRepository = standingReferenceTournamentStageRepository;
        this.standingReferenceTournamentTemplateRepository = standingReferenceTournamentTemplateRepository;
    }

    @Override
    public StandingEntity createFromCommand(RSCommandWrapper<LoadStanding> cmd) {
        return createOrUpdate(cmd.getCommand());
    }

    @Override
    public StandingEntity createTemplate(RSSystemId systemId) {
        throw new RuntimeException("Not implemented yet!");
    }

    @EventListener
    public void onEventPrecedence(PrecedenceLoaded<EventEntity> cmd) {
        standingReferenceEventRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onParticipantPrecedence(PrecedenceLoaded<ParticipantEntity> cmd) {
        Stream.concat(
                standingReferenceParticipantRepository.findByObject(cmd.getSource()),
                standingParticipantRepository.findByParticipant(cmd.getSource()).map(StandingParticipantEntity::getStanding)
        ).forEach(this::publish);
    }

    @EventListener
    public void onSportPrecedence(PrecedenceLoaded<SportEntity> cmd) {
        standingReferenceSportRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onTournamentPrecedence(PrecedenceLoaded<TournamentEntity> cmd) {
        standingReferenceTournamentRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onTournamentStagePrecedence(PrecedenceLoaded<TournamentStageEntity> cmd) {
        standingReferenceTournamentStageRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onTournamentTemplatePrecedence(PrecedenceLoaded<TournamentTemplateEntity> cmd) {
        standingReferenceTournamentTemplateRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    private StandingEntity createOrUpdate(LoadStanding lp) {
        final UUID id = standingRepository.findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId())).map(StandingEntity::getId).orElse(null);
        switch (lp.getObject()) {
            case "participant":
                return createOrUpdateParticipant(lp, id);
            case "event":
                return createOrUpdateEvent(lp, id);
            case "sport":
                return createOrUpdateSport(lp, id);
            case "tournament":
                return createOrUpdateTournament(lp, id);
            case "tournament_stage":
                return createOrUpdateTournamentStage(lp, id);
            case "tournament_template":
                return createOrUpdateTournamentTemplate(lp, id);
            default:
                throw new RuntimeException("Unhandled object: " + lp.getObject());
        }
    }

    private StandingEntity createOrUpdateEvent(LoadStanding lp, UUID id) {
        StandingEvent entity = StandingEvent.builder()
                .id(id)
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(eventService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .config(fromLoadStandingConfigs(lp.getConfig()))
                .standingType(fromLoadStandingType(lp.getStandingType()))
                .properties(lp.getProperties())
                .build();
        standingReferenceEventRepository.save(entity);
        entity.setParticipants(fromLoadStandingParticipants(lp.getSystem(), lp.getParticipants(), entity));
        return entity;
    }

    private StandingEntity createOrUpdateParticipant(LoadStanding ls, UUID id) {
        StandingReferenceParticipant entity = StandingReferenceParticipant.builder()
                .id(id)
                .systemId(RSSystemId.of(ls.getSystem(), ls.getId()))
                .object(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(ls.getSystem(), ls.getObjectId())))
                .config(fromLoadStandingConfigs(ls.getConfig()))
                .standingType(fromLoadStandingType(ls.getStandingType()))
                .properties(ls.getProperties())
                .build();
        standingReferenceParticipantRepository.save(entity);
        entity.setParticipants(fromLoadStandingParticipants(ls.getSystem(), ls.getParticipants(), entity));
        return entity;
    }

    private StandingEntity createOrUpdateSport(LoadStanding lp, UUID id) {
        StandingSport entity = StandingSport.builder()
                .id(id)
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(sportService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .config(fromLoadStandingConfigs(lp.getConfig()))
                .standingType(fromLoadStandingType(lp.getStandingType()))
                .properties(lp.getProperties())
                .build();
        standingReferenceSportRepository.save(entity);
        entity.setParticipants(fromLoadStandingParticipants(lp.getSystem(), lp.getParticipants(), entity));
        return entity;
    }

    private StandingEntity createOrUpdateTournament(LoadStanding lp, UUID id) {
        StandingTournament entity = StandingTournament.builder()
                .id(id)
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(tournamentService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .config(fromLoadStandingConfigs(lp.getConfig()))
                .standingType(fromLoadStandingType(lp.getStandingType()))
                .properties(lp.getProperties())
                .build();
        standingReferenceTournamentRepository.save(entity);
        entity.setParticipants(fromLoadStandingParticipants(lp.getSystem(), lp.getParticipants(), entity));
        return entity;
    }

    private StandingEntity createOrUpdateTournamentStage(LoadStanding lp, UUID id) {
        StandingTournamentStage entity = StandingTournamentStage.builder()
                .id(id)
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(tournamentStageService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .config(fromLoadStandingConfigs(lp.getConfig()))
                .standingType(fromLoadStandingType(lp.getStandingType()))
                .properties(lp.getProperties())
                .build();
        standingReferenceTournamentStageRepository.save(entity);
        entity.setParticipants(fromLoadStandingParticipants(lp.getSystem(), lp.getParticipants(), entity));
        return entity;
    }

    private StandingEntity createOrUpdateTournamentTemplate(LoadStanding lp, UUID id) {
        StandingTournamentTemplate entity = StandingTournamentTemplate.builder()
                .id(id)
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(tournamentTemplateService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .config(fromLoadStandingConfigs(lp.getConfig()))
                .standingType(fromLoadStandingType(lp.getStandingType()))
                .properties(lp.getProperties())
                .build();
        standingReferenceTournamentTemplateRepository.save(entity);
        entity.setParticipants(fromLoadStandingParticipants(lp.getSystem(), lp.getParticipants(), entity));
        return entity;
    }

    private StandingEntity.StandingConfig fromLoadStandingConfig(StandingConfig config) {
        return new StandingEntity.StandingConfig(config.getValue(), config.getSubParam(), fromLoadStandingTypeParam(config.getStandingTypeParam()));
    }

    private List<StandingEntity.StandingConfig> fromLoadStandingConfigs(List<StandingConfig> config) {
        return Optional.ofNullable(config)
                .stream()
                .flatMap(Collection::stream)
                .map(this::fromLoadStandingConfig)
                .collect(Collectors.toList());
    }

    private StandingParticipantEntity.StandingData fromLoadStandingData(StandingData data) {
        return new StandingParticipantEntity.StandingData(data.getCode(), data.getValue(), data.getSubParam());
    }

    private StandingParticipantEntity fromLoadStandingParticipant(DataSystem system, LoadStandingParticipant participant, StandingEntity standing) {
        ParticipantEntity p = participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(system, participant.getId()));
        return standingParticipantRepository.save(
                standingParticipantRepository.findByStandingAndParticipant(standing, p)
                        .orElseGet(StandingParticipantEntity::new)
                        .toBuilder()
                        .participant(p)
                        .rank(participant.getRank())
                        .standing(standing)
                        .data(fromLoadStandingsData(participant.getData()))
                        .properties(participant.getProperties())
                        .build()
        );
    }

    private Set<StandingParticipantEntity> fromLoadStandingParticipants(DataSystem system, List<LoadStandingParticipant> participants, StandingEntity standing) {
        return Optional.ofNullable(participants)
                .stream()
                .flatMap(Collection::stream)
                .map(p -> fromLoadStandingParticipant(system, p, standing))
                .collect(Collectors.toSet());
    }

    private StandingEntity.StandingType fromLoadStandingType(StandingType standingType) {
        return new StandingEntity.StandingType(standingType.getName(), standingType.getDescription());
    }

    private StandingEntity.StandingTypeParam fromLoadStandingTypeParam(StandingTypeParam stp) {
        return new StandingEntity.StandingTypeParam(stp.getName(), stp.getCode(), stp.getValue(), stp.getType());
    }

    private List<StandingParticipantEntity.StandingData> fromLoadStandingsData(List<StandingData> data) {
        return Optional.ofNullable(data)
                .stream()
                .flatMap(Collection::stream)
                .map(this::fromLoadStandingData)
                .collect(Collectors.toList());
    }
}
