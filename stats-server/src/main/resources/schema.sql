CREATE TABLE IF NOT EXISTS hits
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app       VARCHAR(64)                 NOT NULL CHECK (app <> ''),
    uri       VARCHAR(64)                 NOT NULL CHECK (uri <> ''),
    ip        VARCHAR(15)                 NOT NULL CHECK (ip <> ''),
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
