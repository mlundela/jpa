CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists participant_relation
(
    id               uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id        uuid references participant_relation (id),
    system           text NOT NULL,
    system_id        text NOT NULL,
    deleted          bool NOT NULL default false,
    created          timestamp,
    updated          timestamp,
    "from"           timestamp,
    "to"             timestamp,
    active           bool NOT NULL default false,
    type             text NOT NULL,
    object_type      text NOT NULL,
    participant_id   uuid not null,
    object_id        uuid not null,
    properties       hstore,
    primary key (id)
);