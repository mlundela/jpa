package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadIncident;
import no.tv2.sport.resultatservice.domain.model.DataSystem;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class IncidentService extends RSService<RSCommandWrapper<LoadIncident>, IncidentEntity> {

    private final IncidentRepository incidentRepository;
    private final EventService eventService;
    private final ParticipantService participantService;

    public IncidentService(IncidentRepository incidentRepository, ApplicationEventPublisher publisher, EventService eventService, ParticipantService participantService) {
        super(incidentRepository, publisher);
        this.incidentRepository = incidentRepository;
        this.eventService = eventService;
        this.participantService = participantService;
    }

    @Override
    public IncidentEntity createFromCommand(RSCommandWrapper<LoadIncident> wrapper) {
        final LoadIncident cmd = wrapper.getCommand();
        final RSSystemId systemId = wrapper.getSystemId();
        return IncidentEntity.builder()
                .systemId(systemId)
                .sortOrder(cmd.getSortOrder())
                .incidentType(cmd.getIncidentType())
                .elapsedTime(cmd.getElapsedTime())
                .event(eventService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(cmd.getSystem(), cmd.getEventId())))
                .participant(getParticipant(cmd.getSystem(), cmd.getParticipantId()))
                .referencedParticipant(getParticipant(cmd.getSystem(), cmd.getReferencedParticipantId()))
                .properties(cmd.getProperties())
                .deleted(cmd.getDeleted())
                .build();
    }

    @Override
    public IncidentEntity createTemplate(RSSystemId systemId) {
        return IncidentEntity.builder().systemId(systemId).build();
    }

    @EventListener
    public void onEventPrecedence(PrecedenceLoaded<EventEntity> cmd) {
        incidentRepository
                .findByEvent(cmd.getSource())
                .forEach(this::publish);
    }

    @EventListener
    public void onParticipantPrecedence(PrecedenceLoaded<ParticipantEntity> cmd) {
        incidentRepository
                .findByParticipant(cmd.getSource())
                .forEach(this::publish);
        incidentRepository
                .findByReferencedParticipant(cmd.getSource())
                .forEach(this::publish);
    }

    private ParticipantEntity getParticipant(DataSystem system, String systemId) {
        if (systemId == null) {
            return null;
        }
        return participantService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(system, systemId));
    }

}

