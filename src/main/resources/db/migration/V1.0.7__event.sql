CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists event
(
    id                    uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id             uuid references event (id),
    system                text NOT NULL,
    system_id             text NOT NULL,
    deleted               bool NOT NULL default false,
    created               timestamp,
    updated               timestamp,
    name                  text NOT NULL,
    start_date            timestamp,
    status                text NOT NULL,
    status_description    text NOT NULL,
    venue_neutral_ground  bool NOT NULL default false,
    tournament_stage_id   uuid,
    venue_id              uuid,
    properties            hstore,
    primary key (id),
    constraint event_system_uniq_id unique (system, system_id)
);