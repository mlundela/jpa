CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists tournament
(
    id                       uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id                uuid references tournament (id),
    system                   text NOT NULL,
    system_id                text NOT NULL,
    deleted                  bool NOT NULL default false,
    created                  timestamp,
    updated                  timestamp,
    name                     text NOT NULL,
    tournament_template_id   uuid,
    properties               hstore,
    primary key (id),
    constraint tournament_system_uniq_id unique (system, system_id)
);
