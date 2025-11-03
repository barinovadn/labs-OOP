CREATE TABLE computed_points (
    point_id SERIAL PRIMARY KEY,
    function_id INTEGER NOT NULL,
    x_value DECIMAL(15, 8) NOT NULL,
    y_value DECIMAL(15, 8) NOT NULL,
    computed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (function_id) REFERENCES functions(function_id) ON DELETE CASCADE
);