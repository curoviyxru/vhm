CREATE TABLE public.teachers
(
    id serial NOT NULL,
    first_name character varying NOT NULL,
    second_name character varying NOT NULL,
    middle_name character varying,
    email character varying,
    phone_number character varying,
    CONSTRAINT teachers_pk PRIMARY KEY (id)
);

CREATE TABLE public.students
(
    id serial NOT NULL,
    first_name character varying NOT NULL,
    second_name character varying NOT NULL,
    middle_name character varying,
    email character varying,
    phone_number character varying,
    CONSTRAINT students_pk PRIMARY KEY (id)
);

CREATE TABLE public.courses
(
    id serial NOT NULL,
    title character varying NOT NULL,
    description character varying,
    CONSTRAINT courses_pk PRIMARY KEY (id)
);

CREATE TABLE public.courses_schedules
(
    id serial NOT NULL,
    course_id integer NOT NULL,
    title character varying NOT NULL,
    description character varying,
    teacher_id integer NOT NULL,
    date timestamp with time zone NOT NULL,
    CONSTRAINT courses_schedule_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.courses_schedules
    ADD CONSTRAINT course_id FOREIGN KEY (course_id)
    REFERENCES public.courses (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.courses_schedules
    ADD CONSTRAINT teacher_id FOREIGN KEY (teacher_id)
    REFERENCES public.teachers (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

CREATE TABLE public.courses_enrollment
(
    id serial NOT NULL,
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    CONSTRAINT courses_enrollment_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.courses_enrollment
    ADD CONSTRAINT student_id FOREIGN KEY (student_id)
    REFERENCES public.students (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.courses_enrollment
    ADD CONSTRAINT course_id FOREIGN KEY (course_id)
    REFERENCES public.courses (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

CREATE TABLE public.courses_homeworks
(
    id serial NOT NULL,
    course_id integer NOT NULL,
    description character varying NOT NULL,
    CONSTRAINT homeworks_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.courses_homeworks
    ADD CONSTRAINT course_id FOREIGN KEY (course_id)
    REFERENCES public.courses (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

CREATE TABLE public.courses_marks
(
    id serial NOT NULL,
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    teacher_id integer NOT NULL,
    homework_id integer NOT NULL,
    mark integer NOT NULL,
    CONSTRAINT courses_marks_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.courses_marks
    ADD CONSTRAINT student_id FOREIGN KEY (student_id)
    REFERENCES public.students (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.courses_marks
    ADD CONSTRAINT course_id FOREIGN KEY (course_id)
    REFERENCES public.courses (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.courses_marks
    ADD CONSTRAINT teacher_id FOREIGN KEY (teacher_id)
    REFERENCES public.teachers (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.courses_marks
    ADD CONSTRAINT homework_id FOREIGN KEY (homework_id)
    REFERENCES public.courses_homeworks (id) MATCH FULL
    ON UPDATE CASCADE
       ON DELETE CASCADE;