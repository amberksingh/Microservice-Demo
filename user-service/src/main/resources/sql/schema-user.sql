-- Create table if missing
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    balance DOUBLE,
    phone VARCHAR(20),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

---- Composite index
--CREATE INDEX IF NOT EXISTS idx_user_name_email
--ON users (name, email);