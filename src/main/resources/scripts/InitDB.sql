-- 1. Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Таблица ролей
CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

-- Вставка ролей
INSERT INTO roles (role_name, description) VALUES 
    ('ADMIN', 'Full access to all operations'),
    ('USER', 'Can manage own data'),
    ('OPERATOR', 'Read access to all, write access to own data')
ON CONFLICT (role_name) DO NOTHING;

-- 3. Таблица связей пользователей и ролей
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- 4. Таблица функций
CREATE TABLE IF NOT EXISTS functions (
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

-- 5. Таблица вычисленных точек
CREATE TABLE IF NOT EXISTS computed_points (
    point_id SERIAL PRIMARY KEY,
    function_id INTEGER NOT NULL,
    x_value DECIMAL(15, 8) NOT NULL,
    y_value DECIMAL(15, 8) NOT NULL,
    computed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (function_id) REFERENCES functions(function_id) ON DELETE CASCADE
);

-- 6. Таблица композитных функций
CREATE TABLE IF NOT EXISTS composite_functions (
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

-- Создание пользователя admin
INSERT INTO users (username, password, email) VALUES 
    ('admin', 'admin', 'admin@system.com')
ON CONFLICT (username) DO NOTHING;

-- Назначение роли ADMIN пользователю admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id 
FROM users u, roles r 
WHERE u.username = 'admin' AND r.role_name = 'ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;