-- 1. テストユーザーの作成 (パスワードは 'password' の想定)
INSERT INTO users (email, password_hash) VALUES 
('test@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00GdRPHuLPz876');

-- 2. トークン（パスワードリセット用などのサンプル）
INSERT INTO user_auth_tokens (user_id, token_type, token_hash, expires_at) VALUES 
(1, 'reset', 'sample_token_hash_123', DATE_ADD(NOW(), INTERVAL 1 DAY));
