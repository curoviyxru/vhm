CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.organizations
(
    id serial NOT NULL,
    name character varying NOT NULL UNIQUE,
    CONSTRAINT organizations_pk PRIMARY KEY (id)
);

CREATE TABLE public.products
(
    id serial NOT NULL,
    name character varying NOT NULL,
    org_id integer NOT NULL,
    amount integer NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (id)
);