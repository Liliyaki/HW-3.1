-- liquibase formatted sql

-- changeset lkimbl:1
CREATE TABLE student (
id SERIAL,
name TEXT,
age INTEGER
);
CREATE TABLE faculty (
id SERIAL,
name TEXT,
color TEXT
);
-- changeset lkimbl:2
CREATE INDEX student_name_index ON student (name);

CREATE INDEX faculty_name_color_index ON faculty (name,color);