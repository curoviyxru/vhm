INSERT INTO security.roles (
    id, role) VALUES (
                                               '1'::integer, 'guest'::character varying)
returning id;
INSERT INTO security.roles (
    id, role) VALUES (
                                               '2'::integer, 'manager'::character varying)
returning id;

INSERT INTO security.user_roles (
    user_id, role_id) VALUES (
                         '1'::integer, '1'::integer)
returning role_id;
INSERT INTO security.user_roles (
    user_id, role_id) VALUES (
                         '2'::integer, '2'::integer)
returning role_id;

INSERT INTO security.users (
    id, name, password) VALUES (
                                   '1'::integer, 'iamguest'::character varying, 'iamguest'::character varying)
returning id;
INSERT INTO security.users (
    id, name, password) VALUES (
                                   '2'::integer, 'iammanager'::character varying, 'iammanager'::character varying)
returning id;