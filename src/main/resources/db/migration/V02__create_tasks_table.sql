-- Create TASKS table
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL CHECK (char_length(title) >= 5),
    description TEXT,
    status VARCHAR(10) NOT NULL CHECK (status IN ('TODO', 'DOING', 'DONE')),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    due_date DATE,
    project_id INTEGER NOT NULL REFERENCES project(id) ON DELETE CASCADE
);

-- For documentation when using software like pgAdmin or DBeaver
COMMENT ON TABLE tasks IS 'Stores tasks linked to projects.';

COMMENT ON COLUMN tasks.id IS 'Unique task identifier (UUID).';
COMMENT ON COLUMN tasks.title IS 'Task title (5–150 characters, required).';
COMMENT ON COLUMN tasks.description IS 'Optional task details.';
COMMENT ON COLUMN tasks.status IS 'Task status: TODO / DOING / DONE.';
COMMENT ON COLUMN tasks.priority IS 'Task priority: LOW / MEDIUM / HIGH.';
COMMENT ON COLUMN tasks.due_date IS 'Task due date.';
COMMENT ON COLUMN tasks.project_id IS 'Foreign key referencing project.id.';