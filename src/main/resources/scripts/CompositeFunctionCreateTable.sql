CREATE TABLE composite_functions (
    composite_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    composite_name VARCHAR(100) NOT NULL,
    first_function_id INTEGER NOT NULL,
    second_function_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (first_function_id) REFERENCES functions(function_id) ON DELETE CASCADE,
    FOREIGN KEY (second_function_id) REFERENCES functions(function_id) ON DELETE CASCADE
);