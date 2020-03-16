CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists incident
(
    id                          uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id                   uuid references incident (id),
    system                      text NOT NULL,
    system_id                   text NOT NULL,
    deleted                     bool NOT NULL default false,
    created                     timestamp,
    updated                     timestamp,
    incident_type               text NOT NULL,
    elapsed_time                int,
    sort_order                  int  NOT NULL,
    event_id                    uuid,
    participant_id              uuid,
    referenced_participant_id   uuid,
    properties                  hstore,
    primary key (id),
    constraint incident_system_uniq_id unique (system, system_id)
);
