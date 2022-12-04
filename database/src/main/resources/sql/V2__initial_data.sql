INSERT INTO public.organizations (
    name) VALUES (
                                         'Kokia'::character varying)
returning id;
INSERT INTO public.organizations (
    name) VALUES (
                                         'Eoppl'::character varying)
returning id;
INSERT INTO public.organizations (
    name) VALUES (
                                         'Ximi'::character varying)
returning id;
INSERT INTO public.organizations (
    name) VALUES (
                                         'Gnusmas'::character varying)
returning id;

INSERT INTO public.products (
    name, org_id, amount) VALUES (
                           '10.9 6G'::character varying, '1'::integer, '500'::integer)
returning id;
INSERT INTO public.products (
    name, org_id, amount) VALUES (
                           'ePhone 14 Super Puper'::character varying, '2'::integer, '1000'::integer)
returning id;
INSERT INTO public.products (
    name, org_id, amount) VALUES (
                           'Reimu 11T'::character varying, '3'::integer, '1200'::integer)
returning id;
INSERT INTO public.products (
    name, org_id, amount) VALUES (
                           'Space C22'::character varying, '4'::integer, '800'::integer)
returning id;