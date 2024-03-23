CREATE TABLE users
(
    id         BIGSERIAL    NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    name       VARCHAR(100) NOT NULL,
    username   VARCHAR(20)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN') NOT NULL,

    CONSTRAINT user_id_pkey PRIMARY KEY (id),
    CONSTRAINT user_username_ukey UNIQUE (username),
    CONSTRAINT user_email_ukey UNIQUE (email)
);