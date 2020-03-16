CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists venue
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id    uuid references venue (id),
    system       text NOT NULL,
    system_id    text NOT NULL,
    deleted      bool NOT NULL default false,
    created      timestamp,
    updated      timestamp,
    name         text NOT NULL default '',
    country_id   uuid,
    properties   hstore,
    primary key (id),
    constraint venue_system_uniq_id unique (system, system_id)
);
