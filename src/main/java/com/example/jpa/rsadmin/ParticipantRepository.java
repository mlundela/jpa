package com.example.jpa.rsadmin;

import no.tv2.sport.resultatservice.domain.model.DataSystem;
import no.tv2.sport.resultatservice.domain.model.ParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface ParticipantRepository extends RSRepository<ParticipantEntity> {

    Stream<ParticipantEntity> findByCountry(CountryEntity country);

    Page<ParticipantEntity> findBySystemIdSystem(DataSystem system, Pageable pageable);

    List<ParticipantEntity> findByTypeAndCountryId(ParticipantType team, UUID countryId);

    @Query(value =
            "SELECT Cast(p.id as varchar), p.name, p.system, p.system_id as systemId FROM participant p " +
                    "WHERE system=:system " +
                    "AND SIMILARITY(name, :name) > 0.05 " +
                    "ORDER BY SIMILARITY(name, :name) " +
                    "DESC LIMIT 5", nativeQuery = true)
    List<ParticipantSearchResult> searchBySystemAndName(@Param("system") String system, @Param("name") String name);

    interface ParticipantSearchResult {
        String getId();

        String getName();

        DataSystem getSystem();

        String getSystemId();
    }
}
