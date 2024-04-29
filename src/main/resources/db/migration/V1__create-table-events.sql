CREATE TABLE events (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    detailS VARCHAR(255),
    slug VARCHAR(255) NOT NULL,
    maximum_attendees INTEGER NOT NULL
);