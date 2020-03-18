create table if not exists animal (
    id         serial PRIMARY KEY,
    object     text NOT NULL,
    name       text NOT NULL,
    has_banana bool default false,
    is_angry   bool default false
);
