create EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists standing
(
    id            uuid NOT NULL DEFAULT uuid_generate_v4(),
    parent_id     uuid references standing (id),
    system        text NOT NULL,
    system_id     text NOT NULL,
    deleted       bool NOT NULL default false,
    created       timestamp,
    updated       timestamp,
    standing_type jsonb,
    config        jsonb,
    object        text NOT NULL,
    object_id     uuid NOT NULL,
    properties    hstore,
    primary key (id)
);