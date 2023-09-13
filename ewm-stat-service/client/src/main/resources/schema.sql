ALTER TABLE IF EXISTS request DROP CONSTRAINT IF EXISTS request_requester_id;
ALTER TABLE IF EXISTS request DROP CONSTRAINT IF EXISTS request_event_id;
ALTER TABLE IF EXISTS events DROP CONSTRAINT IF EXISTS events_category_fk;
ALTER TABLE IF EXISTS events DROP CONSTRAINT IF EXISTS events_location_fk;
ALTER TABLE IF EXISTS events DROP CONSTRAINT IF EXISTS events_users_fk;
ALTER TABLE IF EXISTS event_compilations DROP CONSTRAINT IF EXISTS event_compilations_event_fk;
ALTER TABLE IF EXISTS event_compilations DROP CONSTRAINT IF EXISTS event_compilations_compilation_fk;

DROP TABLE IF EXISTS event_compilations;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS compilation;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(512) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilation (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL,
    CONSTRAINT uq_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS location (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    confirmed_requests BIGINT,
    created_on TIMESTAMP,
    description VARCHAR(7000),
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    paid boolean NOT NULL,
    participant_limit BIGINT,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(64),
    title VARCHAR(120) NOT NULL,
    views BIGINT
);

CREATE TABLE IF NOT EXISTS event_compilations (
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS request (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    created TIMESTAMP,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status varchar(64)
);

ALTER TABLE events ADD CONSTRAINT events_category_fk FOREIGN KEY (category_id)
REFERENCES category (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE events ADD CONSTRAINT events_location_fk FOREIGN KEY (initiator_id)
REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE events ADD CONSTRAINT events_users_fk FOREIGN KEY (location_id)
REFERENCES location (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE event_compilations ADD CONSTRAINT event_compilations_event_fk FOREIGN KEY (event_id)
REFERENCES events (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE event_compilations ADD CONSTRAINT event_compilations_compilation_fk FOREIGN KEY (compilation_id)
REFERENCES compilation (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE request ADD CONSTRAINT request_requester_id FOREIGN KEY (requester_id)
REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE request ADD CONSTRAINT request_event_id FOREIGN KEY (event_id)
REFERENCES events (id) ON UPDATE CASCADE ON DELETE CASCADE;