-- ユーザーに関するSQL文
SELECT * FROM omakakakeibo_db.users;

-- 1. ユーザーテーブル作成
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    login_id VARCHAR(255) UNIQUE,
    password_hash TEXT,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. テストユーザーの作成 (パスワードは 'password' の想定)
INSERT INTO users (login_id, password_hash) VALUES 
('test@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00GdRPHuLPz876');

-- ===========================
--  3. エラーテスト用
-- ===========================
-- 【期待されるエラー】Duplicate entry 'test@example.com' for key 'email'
INSERT INTO users (login_id, password_hash) 
VALUES ('test@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00GdRPHuLPz876');

-- 【期待されるエラー】Data too long for column 'email' at row 1
INSERT INTO users (login_id, password_hash) 
VALUES (CONCAT(REPEAT('a', 255), '@example.com'), 'dummy_hash');

