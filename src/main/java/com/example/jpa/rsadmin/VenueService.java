package com.example.jpa.rsadmin;

import lombok.extern.slf4j.Slf4j;
import no.tv2.sport.resultatservice.domain.command.LoadVenue;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class VenueService extends RSService<RSCommandWrapper<LoadVenue>, VenueEntity> {

    private final VenueRepository venueRepository;
    private final CountryService countryService;

    public VenueService(ApplicationEventPublisher publisher, VenueRepository venueRepository, CountryService countryService) {
        super(venueRepository, publisher);
        this.venueRepository = venueRepository;
        this.countryService = countryService;
    }

    @Override
    public VenueEntity createFromCommand(RSCommandWrapper<LoadVenue> wrapper) {
        final LoadVenue c = wrapper.getCommand();
        return VenueEntity.builder().systemId(RSSystemId.of(c.getSystem(), c.getId()))
                .name(c.getName())
                .country(countryService.findBySystemAndSystemIdOrCreateTemplate(RSSystemId.of(c.getSystem(), c.getCountryId())))
                .deleted(c.getDeleted())
                .properties(c.getProperties())
                .build();
    }

    @Override
    public VenueEntity createTemplate(RSSystemId systemId) {
        return VenueEntity.builder()
                .systemId(systemId)
                .name("")
                .properties(Map.of())
                .build();
    }


    @EventListener
    public void onCountryPrecedence(PrecedenceLoaded<CountryEntity> cmd) {
        venueRepository.findByCountry(cmd.getSource()).forEach(this::publish);
    }

}

