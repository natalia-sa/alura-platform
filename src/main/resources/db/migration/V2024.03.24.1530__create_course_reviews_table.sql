CREATE TABLE course_reviews
(
    id            INT    PRIMARY KEY AUTO_INCREMENT,
    created_at    TIMESTAMP  NOT NULL DEFAULT NOW(),
    user_id       INT     NOT NULL,
    course_id     INT     NOT NULL,
    rating        INTEGER    NOT NULL,
    comment       TEXT,

    CONSTRAINT course_review_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT course_review_course_id_fkey FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT course_review_user_id_course_id_ukey UNIQUE (user_id, course_id)
);