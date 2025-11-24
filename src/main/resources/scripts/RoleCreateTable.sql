CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

INSERT INTO roles (role_name, description) VALUES 
    ('ADMIN', 'Full access to all operations'),
    ('USER', 'Can manage own data'),
    ('OPERATOR', 'Read access to all, write access to own data')
ON CONFLICT (role_name) DO NOTHING;


