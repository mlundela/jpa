package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadEvent;
import no.tv2.sport.resultatservice.domain.command.LoadEventLineup;
import no.tv2.sport.resultatservice.domain.command.LoadEventParticipant;
import no.tv2.sport.resultatservice.domain.model.DataSystem;
import no.tv2.sport.resultatservice.domain.model.EventStatus;
import no.tv2.sport.resultatservice.domain.model.EventStatusDescription;
import no.tv2.sport.resultatservice.domain.model.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class EventService extends RSService<RSCommandWrapper<LoadEvent>, EventEntity> {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventParticipantLineupRepository eventParticipantLineupRepository;
    private final ParticipantService participantService;
    private final TournamentStageService tournamentStageService;
    private final VenueService venueService;

    public EventService(ApplicationEventPublisher publisher, EventRepository eventRepository, EventParticipantRepository eventParticipantRepository, EventParticipantLineupRepository eventParticipantLineupRepository, ParticipantService participantService, TournamentStageService tournamentStageService, VenueService venueService) {
        super(eventRepository, publisher);
        this.eventRepository = eventRepository;
        this.eventParticipantRepository = eventParticipantRepository;
        this.eventParticipantLineupRepository = eventParticipantLineupRepository;
        this.participantService = participantService;
        this.tournamentStageService = tournamentStageService;
        this.venueService = venueService;
    }

    @Override
    public EventEntity createFromCommand(RSCommandWrapper<LoadEvent> wrapper) {
        final LoadEvent cmd = wrapper.getCommand();
        final RSSystemId systemId = wrapper.getSystemId();
        EventEntity event = eventRepository.findBySystemId(systemId)
                .orElseGet(EventEntity::new)
                .toBuilder()
                .systemId(systemId)
                .name(cmd.getName())
                .startDate(cmd.getStartDate())
                .status(cmd.getStatus())
                .statusDescription(cmd.getStatusDescription())
                .tournamentStage(tournamentStageService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(cmd.getSystem(), cmd.getTournamentStageId())))
                .venue(venueService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(cmd.getSystem(), cmd.getVenueId())))
                .venueNeutralGround(cmd.getVenueNeutralGround())
                .properties(cmd.getProperties())
                .deleted(cmd.getDeleted())
                .build();
        eventRepository.save(event);
        event.setParticipants(fromLoadEventParticipants(cmd.getSystem(), cmd.getParticipants(), event));
        return event;
    }

    @Override
    public EventEntity createTemplate(RSSystemId systemId) {
        return EventEntity.builder()
                .systemId(systemId)
                .name("")
                .status(EventStatus.UNKNOWN)
                .statusDescription(EventStatusDescription.UNKNOWN)
                .properties(Map.of())
                .build();
    }

    public Stream<EventEntity> findByParticipant(ParticipantEntity participant) {
        return Stream.concat(
                eventParticipantRepository.findByParticipant(participant)
                        .map(EventParticipantEntity::getEvent),
                eventParticipantLineupRepository.findByParticipant(participant)
                        .map(EventParticipantLineup::getEventParticipant)
                        .map(EventParticipantEntity::getEvent)
        );
    }

    @EventListener
    public void onParticipantPrecedence(PrecedenceLoaded<ParticipantEntity> cmd) {
        findByParticipant(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onTournamentStagePrecedence(PrecedenceLoaded<TournamentStageEntity> cmd) {
        eventRepository.findByTournamentStage(cmd.getSource()).forEach(this::publish);
    }

    @EventListener
    public void onVenuePrecedence(PrecedenceLoaded<VenueEntity> cmd) {
        eventRepository.findByVenue(cmd.getSource()).forEach(this::publish);
    }

    private EventParticipantLineup fromLoadEventLineup(DataSystem system, LoadEventLineup lineup, EventParticipantEntity ep) {
        ParticipantEntity p = participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(system, lineup.getParticipantId()));
        return eventParticipantLineupRepository.save(
                eventParticipantLineupRepository.findByEventParticipantAndParticipant(ep, p)
                        .orElseGet(EventParticipantLineup::new)
                        .toBuilder()
                        .participant(p)
                        .eventParticipant(ep)
                        .lineupType(lineup.getLineupType())
                        .position(lineup.getPosition())
                        .shirtNumber(lineup.getShirtNumber())
                        .properties(lineup.getProperties())
                        .build()
        );
    }

    private Set<EventParticipantLineup> fromLoadEventLineups(DataSystem system, List<LoadEventLineup> lineups, EventParticipantEntity ep) {
        return lineups.stream()
                .map(l -> fromLoadEventLineup(system, l, ep))
                .collect(Collectors.toSet());
    }

    private EventParticipantEntity fromLoadEventParticipant(DataSystem system, LoadEventParticipant participant, EventEntity event) {
        ParticipantEntity participantEntity = participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(system, participant.getId()));
        EventParticipantEntity eventParticipant = eventParticipantRepository.findByEventAndParticipant(event, participantEntity)
                .orElseGet(EventParticipantEntity::new)
                .toBuilder()
                .number(participant.getNumber())
                .properties(participant.getProperties())
                .participant(participantEntity)
                .event(event)
                .results(fromLoadEventResults(participant.getResults()))
                .build();

        eventParticipantRepository.save(eventParticipant);
        eventParticipant.setLineup(fromLoadEventLineups(system, participant.getLineup(), eventParticipant));

        return eventParticipantRepository.save(eventParticipant);
    }

    private Set<EventParticipantEntity> fromLoadEventParticipants(DataSystem system, List<LoadEventParticipant> participants, EventEntity event) {
        return participants.stream()
                .map(p -> fromLoadEventParticipant(system, p, event))
                .collect(Collectors.toSet());
    }

    private EventParticipantEntity.EventParticipantResult fromLoadEventResult(Result result) {
        return EventParticipantEntity.EventParticipantResult.builder()
                .resultType(result.getResultType())
                .value(result.getValue())
                .build();
    }

    private Set<EventParticipantEntity.EventParticipantResult> fromLoadEventResults(List<Result> results) {
        return results.stream()
                .map(this::fromLoadEventResult)
                .collect(Collectors.toSet());
    }
}
