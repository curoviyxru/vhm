BEGIN;
INSERT INTO public.teachers (
    first_name, second_name, middle_name, email, phone_number) VALUES (
                                                                          'Илья'::character varying, 'Ильин'::character varying, 'Ильич'::character varying, 'ilya@ilyin.ru'::character varying, '+79503333333'::character varying)
returning id;
INSERT INTO public.teachers (
    first_name, second_name, middle_name, email, phone_number) VALUES (
                                                                          'Петр'::character varying, 'Петров'::character varying, 'Петрович'::character varying, 'petr@petrov.ru'::character varying, '+79502222222'::character varying)
returning id;
INSERT INTO public.teachers (
    first_name, second_name, middle_name, email, phone_number) VALUES (
                                                                          'Иван'::character varying, 'Иванов'::character varying, 'Иванович'::character varying, 'ivan@ivanov.ru'::character varying, '+79501111111'::character varying)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.students (
    first_name, second_name, middle_name, email, phone_number) VALUES (
                                                                          'Андрей'::character varying, 'Осипов'::character varying, 'Олегович'::character varying, 'andrey@mail.ru'::character varying, '+79721531702'::character varying)
returning id;
INSERT INTO public.students (
    first_name, second_name, middle_name, email, phone_number) VALUES (
                                                                          'Анжела'::character varying, 'Шаповалова'::character varying, 'Тарасовна'::character varying, 'angela@mail.ru'::character varying, '+79173922195'::character varying)
returning id;
INSERT INTO public.students (
    first_name, second_name, middle_name, email, phone_number) VALUES (
                                                                          'Алексей'::character varying, 'Рощин'::character varying, 'Максимович'::character varying, 'alexey@mail.ru'::character varying, '+79213560949'::character varying)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.courses (
    title, description) VALUES (
                                   'Тестирование игровых проектов'::character varying, 'По итогу прохождения курса ты получишь понимание, как происходит разработка игры и какую роль в этом играет тестировщик;
Научишься пользоваться наиболее популярными инструментами, такими как Jira, TestRail, GIT, Kibana, Grafana и конечно же Unreal Engine;
Ознакомишься с особенностями работы мобильных и веб-тестировщиков;'::character varying)
returning id;
INSERT INTO public.courses (
    title, description) VALUES (
                                   'Java: от слов к делу'::character varying, 'На программе ты освоишь навыки работы с БД с использованием jOOQ, технику Dependency Injection на примере Guice, получишь практические знания, необходимые для создания HTTP-серверов и REST API, научишься писать распределенные реактивные приложения на платформе Vert.x и тестировать их при помощи JUnit.'::character varying)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.courses_schedules (
    course_id, title, description, teacher_id, date) VALUES (
                                                                '1'::integer, 'Тестирование: первое занятие'::character varying, 'Описание первого занятия по тестированию.'::character varying, '1'::integer, '2022-10-25 18:00:00+03'::timestamp with time zone)
returning id;
INSERT INTO public.courses_schedules (
    course_id, title, description, teacher_id, date) VALUES (
                                                                '1'::integer, 'Тестирование: второе занятие'::character varying, 'Описание второго занятия по тестированию.'::character varying, '2'::integer, '2022-11-01 18:00:00+03'::timestamp with time zone)
returning id;
INSERT INTO public.courses_schedules (
    course_id, title, description, teacher_id, date) VALUES (
                                                                '2'::integer, 'Java: первое занятие'::character varying, 'Описание первого занятия по Java.'::character varying, '1'::integer, '2022-10-26 18:00:00+03'::timestamp with time zone)
returning id;
INSERT INTO public.courses_schedules (
    course_id, title, description, teacher_id, date) VALUES (
                                                                '2'::integer, 'Java: второе занятие'::character varying, 'Описание второго занятия по Java.'::character varying, '3'::integer, '2022-11-02 18:00:00+03'::timestamp with time zone)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.courses_enrollment (
    student_id, course_id) VALUES (
                                      '1'::integer, '1'::integer)
returning id;
INSERT INTO public.courses_enrollment (
    student_id, course_id) VALUES (
                                      '2'::integer, '1'::integer)
returning id;
INSERT INTO public.courses_enrollment (
    student_id, course_id) VALUES (
                                      '3'::integer, '2'::integer)
returning id;
INSERT INTO public.courses_enrollment (
    student_id, course_id) VALUES (
                                      '1'::integer, '2'::integer)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.courses_homeworks (
    course_id, description) VALUES (
                                       '1'::integer, 'Домашняя работа 1 по тестированию'::character varying)
returning id;
INSERT INTO public.courses_homeworks (
    course_id, description) VALUES (
                                       '1'::integer, 'Домашняя работа 2 по тестированию'::character varying)
returning id;
INSERT INTO public.courses_homeworks (
    course_id, description) VALUES (
                                       '2'::integer, 'Домашняя работа 1 по Java'::character varying)
returning id;
INSERT INTO public.courses_homeworks (
    course_id, description) VALUES (
                                       '2'::integer, 'Домашняя работа 2 по Java'::character varying)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.courses_marks (
    student_id, course_id, teacher_id, mark, homework_id) VALUES (
                                                                     '1'::integer, '2'::integer, '3'::integer, '4'::integer, '3'::integer)
returning id;
INSERT INTO public.courses_marks (
    student_id, course_id, teacher_id, mark, homework_id) VALUES (
                                                                     '3'::integer, '2'::integer, '3'::integer, '5'::integer, '3'::integer)
returning id;
INSERT INTO public.courses_marks (
    student_id, course_id, teacher_id, mark, homework_id) VALUES (
                                                                     '2'::integer, '1'::integer, '2'::integer, '5'::integer, '1'::integer)
returning id;
INSERT INTO public.courses_marks (
    student_id, course_id, teacher_id, mark, homework_id) VALUES (
                                                                     '1'::integer, '1'::integer, '2'::integer, '5'::integer, '1'::integer)
returning id;
COMMIT;


BEGIN;
INSERT INTO public.courses_attendance (
    schedule_id, student_id) VALUES (
                                        '3'::integer, '1'::integer)
returning id;
INSERT INTO public.courses_attendance (
    schedule_id, student_id) VALUES (
                                        '3'::integer, '3'::integer)
returning id;
INSERT INTO public.courses_attendance (
    schedule_id, student_id) VALUES (
                                        '1'::integer, '2'::integer)
returning id;
INSERT INTO public.courses_attendance (
    schedule_id, student_id) VALUES (
                                        '1'::integer, '1'::integer)
returning id;
COMMIT;