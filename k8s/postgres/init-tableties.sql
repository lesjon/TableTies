-- Create a new database
CREATE DATABASE table_ties WITH OWNER=table_ties;
GRANT ALL PRIVILEGES ON DATABASE table_ties TO table_ties;
-- Connect to the new database
\c table_ties;

CREATE SCHEMA table_ties AUTHORIZATION table_ties;
GRANT ALL ON SCHEMA table_ties TO table_ties;
ALTER USER table_ties SET search_path = table_ties;


-- Create the keycloakUser tableGroup
CREATE TABLE table_ties.keycloak_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL
);
GRANT ALL PRIVILEGES ON TABLE table_ties.keycloak_user TO table_ties;

-- Create the keycloakUser tableGroup
CREATE TABLE table_ties.event (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    keycloak_user_id INTEGER NOT NULL,
    FOREIGN KEY (keycloak_user_id) REFERENCES table_ties.keycloak_user (id)
);
GRANT ALL PRIVILEGES ON TABLE table_ties.event TO table_ties;

-- Create the person table
CREATE TABLE table_ties.person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    event_id INTEGER NOT NULL,
    FOREIGN KEY (event_id) REFERENCES table_ties.event (id)
);
GRANT ALL PRIVILEGES ON TABLE table_ties.person TO table_ties;

-- Create the relation table
CREATE TABLE table_ties.relation (
    id SERIAL PRIMARY KEY,
    person1_id INTEGER NOT NULL,
    person2_id INTEGER NOT NULL ,
    relation_strength DOUBLE PRECISION NOT NULL,
    event_id INTEGER NOT NULL,
    FOREIGN KEY (person1_id) REFERENCES table_ties.person (id),
    FOREIGN KEY (person2_id) REFERENCES table_ties.person (id),
    FOREIGN KEY (event_id) REFERENCES table_ties.event (id),
    CONSTRAINT person1_person2_unique UNIQUE (person1_id, person2_id),
    CONSTRAINT person1_person2_different CHECK (person1_id <> person2_id),
    CONSTRAINT person1_less_than_person2 CHECK (person1_id < person2_id)
);
GRANT ALL PRIVILEGES ON TABLE table_ties.relation TO table_ties;

-- Create the TableGroup table
CREATE TABLE table_ties.table_group (
    id SERIAL PRIMARY KEY,
    target INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    FOREIGN KEY (event_id) REFERENCES table_ties.event (id)
);
GRANT ALL PRIVILEGES ON TABLE table_ties.table_group TO table_ties;

-- Grant access to all sequences
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA table_ties TO table_ties;
