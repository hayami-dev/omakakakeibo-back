-- パスワードリセット・メアド変更に関するSQL
SELECT * FROM omakakakeibo_db.user_auth_tokens;

-- 1. テーブル作成
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

-- ===========================
--  3. エラーテスト用
-- ===========================
-- 【期待されるエラー】Incorrect integer value: 'NOT_AN_INTEGER' for column 'user_id'
INSERT INTO user_auth_tokens (user_id, token_type, token_hash) 
VALUES ('NOT_AN_INTEGER', 'reset', 'sample_hash');

-- 【期待されるエラー】Incorrect parameter count in the call to native function 'DATE_ADD' 
-- または記法エラー
INSERT INTO user_auth_tokens (user_id, token_type, token_hash, expires_at) 
VALUES (1, 'reset', 'hash', DATE_ADD(NOW(), 'DUMMY_INTERVAL_TEXT'));