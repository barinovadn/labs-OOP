CREATE TABLE functions (
    function_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    function_name VARCHAR(100) NOT NULL,
    function_type VARCHAR(50) NOT NULL,
    function_expression TEXT,
    x_from DECIMAL(15, 8),
    x_to DECIMAL(15, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);