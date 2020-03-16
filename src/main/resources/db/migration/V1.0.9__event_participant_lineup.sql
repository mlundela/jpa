CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists event_participant_lineup
(
    id                   uuid    NOT NULL DEFAULT uuid_generate_v4(),
    created                timestamp,
    updated                timestamp,
    shirt_number           integer NOT NULL,
    position               text    NOT NULL,
    lineup_type            text    NOT NULL,

    event_participant_id uuid    NOT NULL,
    participant_id       uuid    NOT NULL,
    properties             hstore,
    results                jsonb,
    primary key (id),
    constraint event_participant_lineup_event_participant_participant_uniq_id unique (event_participant_id, participant_id)
);