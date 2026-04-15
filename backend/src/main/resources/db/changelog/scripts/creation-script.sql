--liquibase formatted sql

--changeset nulleviy:1
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL
);

--changeset nulleviy:2
CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(200) NOT NULL,
                                     description TEXT,
                                     user_id BIGINT NOT NULL,
                                     completed BOOLEAN DEFAULT FALSE,
                                     completed_at TIMESTAMP,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

--rollback DROP TABLE IF EXISTS tasks;
--rollback DROP TABLE IF EXISTS users;