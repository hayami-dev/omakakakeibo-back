CREATE DATABASE IF NOT EXISTS `omakakakeibo_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `omakakakeibo_db`;

-- 1. ユーザーテーブル
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    password_hash TEXT,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

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
