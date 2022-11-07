CREATE TABLE public.courses_attendance
(
    id serial NOT NULL,
    schedule_id integer NOT NULL,
    student_id integer NOT NULL,
    CONSTRAINT courses_attendance_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.courses_attendance
    ADD CONSTRAINT schedule_id FOREIGN KEY (schedule_id)
        REFERENCES public.courses_schedules (id) MATCH FULL
        ON UPDATE CASCADE
        ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.courses_attendance
    ADD CONSTRAINT student_id FOREIGN KEY (student_id)
        REFERENCES public.students (id) MATCH FULL
        ON UPDATE CASCADE
        ON DELETE CASCADE;