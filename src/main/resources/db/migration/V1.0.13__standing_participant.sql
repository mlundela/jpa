CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists standing_participant
(
    id             uuid NOT NULL DEFAULT uuid_generate_v4(),
    created        timestamp,
    updated        timestamp,
    rank           bigint,
    data           jsonb,
    participant_id uuid not null,
    standing_id    uuid not null,
    properties     hstore,
    primary key (id),
    constraint standing_participant_standing_participant_uniq_id unique (standing_id, participant_id)
);