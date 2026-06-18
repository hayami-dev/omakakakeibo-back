-- 支出の履歴に関するSQL文
SELECT * FROM omakakakeibo_db.histories;

-- 支出明細テーブル
CREATE TABLE histories (
    history_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    category_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    history_date DATE NOT NULL,
    note TEXT, -- 将来用：支出メモ
    is_subscription BOOLEAN DEFAULT FALSE, -- 将来用：サブスクフラグ
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    PRIMARY KEY (`history_id`),
    
    -- ユーザーIDと日付での検索を一瞬で終わらせるための索引
    KEY `idx_histories_user_date` (`user_id`, `history_date`),
    
    -- ユーザーテーブルとの紐付け（退会したら連動削除）
    CONSTRAINT `fk_histories_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `omakakakeibo_db`.`users` (`user_id`)
    ON DELETE CASCADE,
    
    -- マスタテーブルとの紐付け
    CONSTRAINT `fk_histories_category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `omakakakeibo_db`.`categories_master` (`category_id`)
    ON DELETE RESTRICT -- カテゴリマスタが消えないようにする
);

-- =====================================================================
-- 【正常系】通常の初期登録ダミーデータ（テスト成功用）
-- =====================================================================
INSERT INTO histories (user_id, category_id, amount, history_date, note, is_subscription) VALUES 
-- 4月の支出履歴
(1, 1, 5200, '2026-04-10', '4月の固定費（光熱費など）', FALSE),
(1, 2, 3000, '2026-04-15', 'カフェで作業代', FALSE),
(1, 3, 11000, '2026-04-20', '推しのライブBD購入', FALSE),

-- 5月の支出履歴
(1, 1, 4500, '2026-05-02', '食材まとめ買い', FALSE),
(1, 4, 1200, '2026-05-15', '友達とランチ', FALSE),
(1, 2, 980, '2026-05-25', '家計簿アプリの月額課金', TRUE), -- サブスク

-- 6月の支出履歴（今月）
(1, 1, 3200, '2026-06-01', '日用品の購入', FALSE),
(1, 3, 5000, '2026-06-03', '推し活グッズ', FALSE),

-- 2月の支出履歴（合計: 52,280円）
(1, 1, 35000, '2026-02-25', '2月の家賃・固定費按分', TRUE), -- サブスク（固定費）
(1, 1, 4800, '2026-02-10', '食材・日用品まとめ買い', FALSE),
(1, 2, 3500, '2026-02-14', 'バレンタインの自分用チョコ', FALSE),
(1, 3, 6500, '2026-02-20', '推しの限定グッズ予約', FALSE),
(1, 4, 1500, '2026-02-28', '月末のご褒美カフェ', FALSE),
(1, 2, 980, '2026-02-25', '家計簿アプリの月額課金', TRUE), -- サブスク

-- 3月の支出履歴（合計: 54,480円）
(1, 1, 35000, '2026-03-25', '3月の家賃・固定費按分', TRUE), -- サブスク（固定費）
(1, 1, 5200, '2026-03-03', '春物の日用品買い足し', FALSE),
(1, 3, 9800, '2026-03-15', '推しのライブチケット代', FALSE),
(1, 4, 1200, '2026-03-18', '友達と駅前カフェ', FALSE),
(1, 2, 2300, '2026-03-20', '欲しかった本を購入', FALSE),
(1, 2, 980, '2026-03-25', '家計簿アプリの月額課金', TRUE); -- サブスク

-- =====================================================================
-- 0. 事前準備（テスト用の正常データを1件登録する）
-- =====================================================================
-- 💡 以降の重複エラーテストや削除禁止テストで使うための正常なベースデータです
INSERT INTO histories (history_id, user_id, category_id, amount, history_date) 
VALUES (9, 1, 1, 1000, '2026-06-04');


-- =====================================================================
-- 1. history_id (主キー) のテスト
-- =====================================================================

-- ❌ 【主キー重複エラー】すでに存在する ID: 9 でもう一度登録しようとする
-- 想定エラーコード：1062 (Duplicate entry '9' for key 'PRIMARY')
INSERT INTO histories (history_id, user_id, category_id, amount, history_date) 
VALUES (9, 1, 1, 2000, '2026-06-04');


-- =====================================================================
-- 2. user_id (ユーザーID) のテスト
-- =====================================================================

-- ❌ 【データ型エラー】数値型(BIGINT)に対して文字列 'yamada' を叩き込む
-- 想定エラーコード：1366 (Incorrect integer value)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES ('yamada', 1, 1000, '2026-06-04');

-- ❌ 【NOT NULL制約エラー】user_id を NULL (空っぽ) にして登録しようとする
-- 想定エラーコード：1048 (Column 'user_id' cannot be null)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (NULL, 1, 1000, '2026-06-04');

-- ❌ 【外部キー制約エラー】usersテーブルに存在しないダミーのユーザーID：99999 で登録しようとする
-- 想定エラーコード：1452 (Cannot add or update a child row: a foreign key constraint fails...)
INSERT INTO histories (user_id, category_id, amount, history_date, note) 
VALUES (99999, 1, 1500, '2026-06-04', '不正なユーザーデータ');


-- =====================================================================
-- 3. category_id (カテゴリID) のテスト
-- =====================================================================

-- ❌ 【データ型エラー】数値型(BIGINT)に対して文字列 'food' を叩き込む
-- 想定エラーコード：1366 (Incorrect integer value)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (1, 'food', 1000, '2026-06-04');

-- ❌ 【NOT NULL制約エラー】category_id を NULL (空っぽ) にして登録しようとする
-- 想定エラーコード：1048 (Column 'category_id' cannot be null)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (1, NULL, 1000, '2026-06-04');

-- ❌ 【外部キー制約エラー】categories_masterテーブルに存在しないカテゴリID：99999 で登録しようとする
-- 想定エラーコード：1452 (Cannot add or update a child row: a foreign key constraint fails...)
INSERT INTO histories (user_id, category_id, amount, history_date, note) 
VALUES (1, 99999, 2000, '2026-06-04', '不正なカテゴリデータ');


-- =====================================================================
-- 4. amount (金額) のテスト
-- =====================================================================

-- ❌ 【NOT NULL制約エラー】amount を NULL (空っぽ) にして登録しようとする
-- 想定エラーコード：1048 (Column 'amount' cannot be null)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (1, 1, NULL, '2026-06-04');

-- ❌ 【範囲超過エラー】金額に 100兆円（INTEGERの限界値 約21億 を超える数値）を入れて溢れさせる
-- 想定エラーコード：1264 (Out of range value for column 'amount')
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (1, 1, 1000000000000000, '2026-06-04');


-- =====================================================================
-- 5. history_date (日付) のテスト
-- =====================================================================

-- ❌ 【データ型エラー】存在しない日付「2026-02-31」を流し込む
-- 想定エラーコード：1292 (Incorrect date value)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (1, 1, 1000, '2026-02-31');

-- ❌ 【NOT NULL制約エラー】history_date を NULL (空っぽ) にして登録しようとする
-- 想定エラーコード：1048 (Column 'history_date' cannot be null)
INSERT INTO histories (user_id, category_id, amount, history_date) 
VALUES (1, 1, 1000, NULL);


-- =====================================================================
-- 6. is_subscription (サブスクフラグ) のテスト
-- =====================================================================

-- ❌ 【データ型エラー】サブスクフラグ（BOOLEAN型）に「'hello'」という文字列を入れる
-- 想定エラーコード：1366 (Incorrect integer value) または警告
INSERT INTO histories (user_id, category_id, amount, history_date, is_subscription) 
VALUES (1, 1, 1000, '2026-06-04', 'hello');

-- 💡 補足: is_subscription は `DEFAULT FALSE` が設定されているため、
-- 明示的に NULL をINSERTしようとしても、データベースが自動的にデフォルト値(0/FALSE)で埋めて守ってくれます。


-- =====================================================================
-- 7. ON DELETE RESTRICT (連動削除ブロック) のテスト
-- =====================================================================

-- ❌ 【親レコード削除制限エラー】手順0で登録した履歴が残っている状態で、「カテゴリID: 1」をマスタから消そうとする
-- 想定エラーコード：1451 (Cannot delete or update a parent row: a foreign key constraint fails... ON DELETE RESTRICT)
DELETE FROM categories_master WHERE category_id = 1;