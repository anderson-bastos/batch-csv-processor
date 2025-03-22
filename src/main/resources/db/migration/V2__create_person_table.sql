CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);