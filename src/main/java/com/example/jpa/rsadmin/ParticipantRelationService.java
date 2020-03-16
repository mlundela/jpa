package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadParticipantRelation;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Slf4j
public class ParticipantRelationService extends RSService<RSCommandWrapper<LoadParticipantRelation>, ParticipantRelationEntity> {

    private final EventService eventService;
    private final SportService sportService;
    private final TournamentStageService tournamentStageService;
    private final TournamentTemplateService tournamentTemplateService;
    private final VenueService venueService;
    private final ParticipantService participantService;
    private final ParticipantRelationRepository participantRelationRepository;
    private final ParticipantRelationParticipantRepository participantRelationParticipantRepository;
    private final ParticipantRelationEventRepository participantRelationEventRepository;
    private final ParticipantRelationSportRepository participantRelationSportRepository;
    private final ParticipantRelationTournamentStageRepository participantRelationTournamentStageRepository;
    private final ParticipantRelationTournamentTemplateRepository participantRelationTournamentTemplateRepository;
    private final ParticipantRelationVenueRepository participantRelationVenueRepository;

    public ParticipantRelationService(
            EventService eventService,
            SportService sportService,
            TournamentStageService tournamentStageService,
            TournamentTemplateService tournamentTemplateService,
            VenueService venueService,
            ParticipantService participantService,
            ParticipantRelationRepository participantRelationRepository,
            ParticipantRelationParticipantRepository participantRelationParticipantRepository,
            ParticipantRelationEventRepository participantRelationEventRepository,
            ParticipantRelationSportRepository participantRelationSportRepository,
            ParticipantRelationTournamentStageRepository participantRelationTournamentStageRepository,
            ParticipantRelationTournamentTemplateRepository participantRelationTournamentTemplateRepository,
            ParticipantRelationVenueRepository participantRelationVenueRepository,
            ApplicationEventPublisher publisher) {

        super(participantRelationRepository, publisher);
        this.eventService = eventService;
        this.sportService = sportService;
        this.tournamentStageService = tournamentStageService;
        this.tournamentTemplateService = tournamentTemplateService;
        this.venueService = venueService;
        this.participantService = participantService;
        this.participantRelationRepository = participantRelationRepository;
        this.participantRelationParticipantRepository = participantRelationParticipantRepository;
        this.participantRelationEventRepository = participantRelationEventRepository;
        this.participantRelationSportRepository = participantRelationSportRepository;
        this.participantRelationTournamentStageRepository = participantRelationTournamentStageRepository;
        this.participantRelationTournamentTemplateRepository = participantRelationTournamentTemplateRepository;
        this.participantRelationVenueRepository = participantRelationVenueRepository;
    }

    @Override
    public ParticipantRelationEntity createFromCommand(RSCommandWrapper<LoadParticipantRelation> cmd) {
        return createFromCommand(cmd.getCommand());
    }

    @Override
    public ParticipantRelationEntity createTemplate(RSSystemId systemId) {
        throw new RuntimeException("Not implemented yet!");
    }

    @EventListener
    public void onEventPrecedence(PrecedenceLoaded<EventEntity> cmd) {
        participantRelationEventRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onParticipantPrecedence(PrecedenceLoaded<ParticipantEntity> cmd) {
        Stream.concat(
                participantRelationParticipantRepository.findByObject(cmd.getSource()),
                participantRelationRepository.findByParticipant(cmd.getSource())
        ).forEach(this::publish);
    }

    @EventListener
    public void onSportPrecedence(PrecedenceLoaded<SportEntity> cmd) {
        participantRelationSportRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onTournamentStagePrecedence(PrecedenceLoaded<TournamentStageEntity> cmd) {
        participantRelationTournamentStageRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onTournamentTemplatePrecedence(PrecedenceLoaded<TournamentTemplateEntity> cmd) {
        participantRelationTournamentTemplateRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onVenuePrecedence(PrecedenceLoaded<VenueEntity> cmd) {
        participantRelationVenueRepository.findByObject(cmd.getSource()).forEach(this::publish);
    }

    private ParticipantRelationEntity createEvent(LoadParticipantRelation lp) {
        return participantRelationEventRepository
                .findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .orElseGet(ParticipantRelationEvent::new)
                .toBuilder()
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(eventService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .from(lp.getFrom())
                .to(lp.getTo())
                .active(lp.getActive())
                .properties(lp.getProperties())
                .participant(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getParticipantId())))
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }

    private ParticipantRelationEntity createFromCommand(LoadParticipantRelation lp) {
        switch (lp.getObjectType()) {
            case PARTICIPANT:
                return createParticipant(lp);
            case EVENT:
                return createEvent(lp);
            case SPORT:
                return createSport(lp);
            case TOURNAMENT_STAGE:
                return createTournamentStage(lp);
            case TOURNAMENT_TEMPLATE:
                return createTournamentTemplate(lp);
            case VENUE:
                return createVenue(lp);
            default:
                throw new RuntimeException("Unhandled relation object type: " + lp.getObjectType());
        }
    }

    private ParticipantRelationEntity createParticipant(LoadParticipantRelation lp) {
        return participantRelationParticipantRepository
                .findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .orElseGet(ParticipantRelationParticipant::new)
                .toBuilder()
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .from(lp.getFrom())
                .to(lp.getTo())
                .active(lp.getActive())
                .properties(lp.getProperties())
                .participant(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getParticipantId())))
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }

    private ParticipantRelationEntity createSport(LoadParticipantRelation lp) {
        return participantRelationSportRepository
                .findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .orElseGet(ParticipantRelationSport::new)
                .toBuilder()
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(sportService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .from(lp.getFrom())
                .to(lp.getTo())
                .active(lp.getActive())
                .properties(lp.getProperties())
                .participant(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getParticipantId())))
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }

    private ParticipantRelationEntity createTournamentStage(LoadParticipantRelation lp) {
        return participantRelationTournamentStageRepository.findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .orElseGet(ParticipantRelationTournamentStage::new)
                .toBuilder()
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(tournamentStageService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .from(lp.getFrom())
                .to(lp.getTo())
                .active(lp.getActive())
                .properties(lp.getProperties())
                .participant(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getParticipantId())))
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }

    private ParticipantRelationEntity createTournamentTemplate(LoadParticipantRelation lp) {
        return participantRelationTournamentTemplateRepository.findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .orElseGet(ParticipantRelationTournamentTemplate::new)
                .toBuilder()
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(tournamentTemplateService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .from(lp.getFrom())
                .to(lp.getTo())
                .active(lp.getActive())
                .properties(lp.getProperties())
                .participant(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getParticipantId())))
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }

    private ParticipantRelationEntity createVenue(LoadParticipantRelation lp) {
        return participantRelationVenueRepository.findBySystemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .orElseGet(ParticipantRelationVenue::new)
                .toBuilder()
                .systemId(RSSystemId.of(lp.getSystem(), lp.getId()))
                .object(venueService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getObjectId())))
                .from(lp.getFrom())
                .to(lp.getTo())
                .active(lp.getActive())
                .properties(lp.getProperties())
                .participant(participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(lp.getSystem(), lp.getParticipantId())))
                .type(lp.getType())
                .deleted(lp.getDeleted())
                .build();
    }
}
