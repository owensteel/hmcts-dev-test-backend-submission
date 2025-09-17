CREATE TABLE
    IF NOT EXISTS tasks (
        id BIGSERIAL PRIMARY KEY,
        title VARCHAR(120) NOT NULL,
        description TEXT,
        status VARCHAR(20) NOT NULL,
        due_at TIMESTAMPTZ NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks (status);

CREATE INDEX IF NOT EXISTS idx_tasks_due_at ON tasks (due_at);