CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists tournament_template
(
    id         uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id  uuid references tournament_template (id),
    system     text NOT NULL,
    system_id  text NOT NULL,
    deleted    bool NOT NULL default false,
    created    timestamp,
    updated    timestamp,
    name       text NOT NULL,
    gender     text NOT NULL,
    sport_id uuid,
    properties hstore,
    primary key (id),
    constraint tournament_template_system_uniq_id unique (system, system_id)
);
