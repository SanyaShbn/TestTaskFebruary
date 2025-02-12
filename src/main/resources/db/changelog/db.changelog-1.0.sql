--liquibase formatted sql

--changeset shubinalex:1
CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL UNIQUE,
    birth_date DATE,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    role VARCHAR(32)
);