CREATE TABLE nodes(
 id serial PRIMARY KEY,
 data JSONB NOT NULL
);

CREATE TABLE links(
 id serial PRIMARY KEY,
 data JSONB NOT NULL
);