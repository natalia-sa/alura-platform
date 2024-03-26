CREATE TABLE registrations
(
    id            INT    PRIMARY KEY AUTO_INCREMENT,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    user_id       BIGINT NOT NULL,
    course_id     BIGINT  NOT NULL,

    CONSTRAINT registration_user_id_course_id_ukey UNIQUE (user_id, course_id),
    CONSTRAINT registration_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT registration_course_id_fkey FOREIGN KEY (course_id) REFERENCES courses (id)
);