-- ユーザーに関するSQL文
SELECT * FROM omakakakeibo_db.users;
SELECT * FROM omakakakeibo_db.user_auth_tokens;

-- 1. ユーザーテーブル
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    password_hash TEXT,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 1. テストユーザーの作成 (パスワードは 'password' の想定)
INSERT INTO users (email, password_hash) VALUES 
('test@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00GdRPHuLPz876');



-- 2. パスワードリセット・メアド変更用
CREATE TABLE user_auth_tokens (
    token_id SERIAL PRIMARY KEY,
    user_id INTEGER,
    token_type VARCHAR(20), -- 'reset', 'email_change'
    token_hash TEXT,
    new_email VARCHAR(255),
    expires_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
);


-- 2. トークン（パスワードリセット用などのサンプル）
INSERT INTO user_auth_tokens (user_id, token_type, token_hash, expires_at) VALUES 
(1, 'reset', 'sample_token_hash_123', DATE_ADD(NOW(), INTERVAL 1 DAY));
