CREATE TABLE
    tasks (
        id BIGSERIAL PRIMARY KEY,
        title VARCHAR(120) NOT NULL,
        description TEXT,
        status VARCHAR(20) NOT NULL,
        due_at TIMESTAMPTZ NOT NULL
    );