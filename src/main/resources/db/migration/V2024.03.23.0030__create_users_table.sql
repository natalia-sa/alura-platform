CREATE TABLE users
(
    id         INT    PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    name       VARCHAR(100) NOT NULL,
    username   VARCHAR(20)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN') NOT NULL,
    CONSTRAINT user_username_ukey UNIQUE (username),
    CONSTRAINT user_email_ukey UNIQUE (email)
);