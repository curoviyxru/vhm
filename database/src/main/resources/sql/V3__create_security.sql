CREATE SCHEMA IF NOT EXISTS security;

CREATE TABLE IF NOT EXISTS security.users
(
    id integer PRIMARY KEY,
    name character varying NOT NULL UNIQUE,
    password character varying NOT NULL
);

CREATE TABLE IF NOT EXISTS security.roles
(
    id integer PRIMARY KEY,
    role character varying NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS security.user_roles
(
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    PRIMARY KEY (user_id, role_id)
);