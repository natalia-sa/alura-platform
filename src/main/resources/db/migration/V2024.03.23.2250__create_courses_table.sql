CREATE TABLE courses
(
    id            BIGSERIAL    NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    name          VARCHAR(100) NOT NULL,
    code          VARCHAR(10)  NOT NULL,
    instructor_id BIGINT       NOT NULL,
    description   TEXT         NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    inactivated_at TIMESTAMP,

    CONSTRAINT course_id_pkey PRIMARY KEY (id),
    CONSTRAINT course_code_ukey UNIQUE (code),
    CONSTRAINT course_instructor_id_fkey FOREIGN KEY (instructor_id) REFERENCES users (id)
);