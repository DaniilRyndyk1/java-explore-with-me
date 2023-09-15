DROP TABLE IF EXISTS endpoint_hit;
CREATE TABLE IF NOT EXISTS endpoint_hit (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    app varchar(512)  NOT NULL,
    uri varchar(512) NOT NULL,
    ip varchar(512) NOT NULL,
    created TIMESTAMP NOT NULL
);