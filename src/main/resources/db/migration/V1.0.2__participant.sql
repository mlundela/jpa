create EXTENSION IF NOT EXISTS hstore;
create EXTENSION IF NOT EXISTS "uuid-ossp";
create EXTENSION IF NOT EXISTS pg_trgm;


create table if not exists participant
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id    uuid references participant (id),
    system       text NOT NULL,
    system_id    text NOT NULL,
    deleted      bool NOT NULL default false,
    created      timestamp,
    updated      timestamp,
    name         text NOT NULL,
    gender       text NOT NULL,
    type         text NOT NULL,
    country_id uuid,
    properties   hstore,
    primary key (id),
    constraint participant_system_uniq_id unique (system, system_id)
);
