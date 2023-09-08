DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar(512) NOT NULL,
    email varchar(512) NOT NULL
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);

DROP TABLE IF EXISTS events;
CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation varchar(2048) NOT NULL,
    category_id BIGINT REFERENCES category (id) NOT NULL ,
    confirmedRequests BIGINT,
    createdOn TIMESTAMP,
    description varchar(2048),
    eventDate TIMESTAMP NOT NULL,
    initiator_id BIGINT REFERENCES users (id) NOT NULL,
    location_id BIGINT REFERENCES location (id) NOT NULL,
    paid boolean NOT NULL,
    participantLimit integer,
    publishedOn TIMESTAMP,
    requestModeration boolean,
    state varchar(64),
    title varchar(2048) NOT NULL,
    views BIGINT,
    CONSTRAINT pk_users PRIMARY KEY (id)
);