INSERT INTO public.products (
    code, name) VALUES (
                           '1'::integer, 'Kokia 10.9 6G'::character varying)
returning code;
INSERT INTO public.products (
    code, name) VALUES (
                           '2'::integer, 'ePhone 14 Super Puper'::character varying)
returning code;
INSERT INTO public.products (
    code, name) VALUES (
                           '3'::integer, 'Ximi Reimu 11T'::character varying)
returning code;
INSERT INTO public.products (
    code, name) VALUES (
                           '4'::integer, 'Gnusmas Space C22'::character varying)
returning code;

INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '111111'::integer, 'N.Video Store'::character varying, '614615097140'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '222222'::integer, 'DNC Store'::character varying, '908416510931'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '333333'::integer, 'reShop Store'::character varying, '234790619471'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '444444'::integer, 'Online-exchange Store'::character varying, '129360150954'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '555555'::integer, 'Random Store'::character varying, '1351516316'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '666666'::integer, 'Goldencity Store'::character varying, '13413515135'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '777777'::integer, 'Balislowness Store'::character varying, '1353516513635'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '888888'::integer, 'Noname Store'::character varying, '14151356316'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '999999'::integer, 'FastStore'::character varying, '14351516'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '1000000'::integer, 'GeeksStore'::character varying, '2353153141'::character varying)
returning inn;
INSERT INTO public.organizations (
    inn, name, giro) VALUES (
                                '1111111'::integer, 'SmartBuy Store'::character varying, '123146514151'::character varying)
returning inn;

INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '5'::integer, '2022-04-06'::date, '333333'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '6'::integer, '2022-04-05'::date, '333333'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '7'::integer, '2022-01-02'::date, '111111'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '8'::integer, '2022-01-01'::date, '111111'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '3'::integer, '2022-09-02'::date, '555555'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '2'::integer, '2022-09-03'::date, '222222'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '1'::integer, '2022-01-05'::date, '444444'::integer)
returning id;
INSERT INTO public.receipts (
    id, date, organization_id) VALUES (
                                          '4'::integer, '2022-01-03'::date, '111111'::integer)
returning id;

INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '3'::integer, '8'::integer, '2'::integer, '63999'::double precision, '500'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '2'::integer, '7'::integer, '4'::integer, '67700'::double precision, '250'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '1'::integer, '2'::integer, '2'::integer, '59999.99'::double precision, '400'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '10'::integer, '6'::integer, '1'::integer, '34999.99'::double precision, '300'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '9'::integer, '1'::integer, '3'::integer, '19999.99'::double precision, '1000'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '8'::integer, '5'::integer, '4'::integer, '54999.90'::double precision, '560'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '11'::integer, '3'::integer, '2'::integer, '69999.99'::double precision, '100'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '6'::integer, '3'::integer, '4'::integer, '69999.99'::double precision, '50'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '5'::integer, '2'::integer, '2'::integer, '59999.99'::double precision, '1000'::integer)
returning id;
INSERT INTO public.positions (
    id, receipt_id, product_id, price, amount) VALUES (
                                                          '4'::integer, '5'::integer, '3'::integer, '21999'::double precision, '200'::integer)
returning id;