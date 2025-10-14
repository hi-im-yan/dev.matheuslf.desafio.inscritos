-- Create PROJECT table
CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL CHECK (char_length(name) >= 3),
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE
);

-- For documentation when using software like pgAdmin or DBeaver
COMMENT ON TABLE project IS 'Stores information about projects.';

COMMENT ON COLUMN project.id IS 'Unique project identifier (SERIAL).';
COMMENT ON COLUMN project.name IS 'Project name (3–100 characters, required).';
COMMENT ON COLUMN project.description IS 'Optional project description.';
COMMENT ON COLUMN project.start_date IS 'Date when the project starts.';
COMMENT ON COLUMN project.end_date IS 'Optional project end date.';