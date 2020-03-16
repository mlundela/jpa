CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists sport
(
    id        uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id uuid references sport (id),
    system    text NOT NULL,
    system_id text NOT NULL,
    deleted   bool NOT NULL default false,
    created   timestamp,
    updated   timestamp,
    name      text NOT NULL default '',
    properties hstore,
    primary key (id),
    constraint sport_system_uniq_id unique (system, system_id)
);
