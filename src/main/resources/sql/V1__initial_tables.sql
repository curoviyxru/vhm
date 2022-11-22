CREATE TABLE public.organizations
(
    inn integer NOT NULL,
    name character varying NOT NULL,
    giro character varying NOT NULL,
    CONSTRAINT organizations_pk PRIMARY KEY (inn)
);

CREATE TABLE public.products
(
    code integer NOT NULL,
    name character varying NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (code)
);

CREATE TABLE public.receipts
(
    id integer NOT NULL,
    date date NOT NULL,
    organization_id integer NOT NULL,
    CONSTRAINT receipts_pk PRIMARY KEY (id),
    CONSTRAINT organization_id FOREIGN KEY (organization_id)
        REFERENCES public.organizations (inn) MATCH FULL
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE public.positions
(
    id integer NOT NULL,
    receipt_id integer NOT NULL,
    product_id integer NOT NULL,
    price double precision NOT NULL,
    amount integer NOT NULL,
    CONSTRAINT positions_id PRIMARY KEY (id),
    CONSTRAINT receipt_id FOREIGN KEY (receipt_id)
        REFERENCES public.receipts (id) MATCH FULL
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT product_id FOREIGN KEY (product_id)
        REFERENCES public.products (code) MATCH FULL
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
