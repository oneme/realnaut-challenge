CREATE TABLE spaceship
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255),
    series VARCHAR(255),
    type   VARCHAR(255)
);

CREATE TABLE users
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    email      VARCHAR(100),
    password   VARCHAR(255),
    created_at DATE,
    updated_at DATE
)