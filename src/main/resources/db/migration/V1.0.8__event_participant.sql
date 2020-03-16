CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists event_participant
(
    id             uuid    NOT NULL DEFAULT uuid_generate_v4(),
    created          timestamp,
    updated          timestamp,
    number           integer NOT NULL,

    event_id       uuid    not null,
    participant_id uuid    not null,
    properties       hstore,
    results          jsonb,
    primary key (id),
    constraint event_participant_event_participant_uniq_id unique (event_id, participant_id)
);