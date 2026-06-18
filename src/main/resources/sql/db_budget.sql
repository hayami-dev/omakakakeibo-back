-- 月別目標金額テーブルに関するSQL文
SELECT * FROM omakakakeibo_db.monthly_budgets;

-- テーブル作成
CREATE TABLE `omakakakeibo_db`.`monthly_budgets` (
  `budget_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `target_month` CHAR(7) NOT NULL,
  `target_amount` INT NOT NULL DEFAULT 50000,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  PRIMARY KEY (`budget_id`),
  
  -- 複合UNIQUEにして各ユーザーが1ヶ月に1回だけ登録できるようにする
  UNIQUE INDEX `idx_user_month_UNIQUE` (`user_id` ASC, `target_month` ASC) VISIBLE,
  
  -- 外部キー制約をつけて外部テーブルと紐づけする
	CONSTRAINT `fk_budgets_user_id`
	FOREIGN KEY (`user_id`)
	REFERENCES `omakakakeibo_db`.`users` (`user_id`)
	ON DELETE CASCADE
);


-- =====================================================================
-- 1. 【正常系】通常の初期登録ダミーデータ（テスト成功用）
-- =====================================================================
-- 💡 ユーザーID: 1 の過去（3月、4月、5月）の予算を正常に登録します。
-- ※元のデータの日付が少しズレていたため、わかりやすく2026年のデータに調整しました。

INSERT INTO monthly_budgets (user_id, target_month, target_amount, updated_at) VALUES
(1, '2026-03', 45000, '2026-03-01 09:00:00'),
(1, '2026-04', 50000, '2026-04-01 09:00:00'),
(1, '2026-05', 55000, '2026-05-01 09:00:00');


-- =====================================================================
-- 2. 【異常系】仕様・制約をすり抜けるゴミデータを防ぐエラーテスト
-- =====================================================================
-- 💡 1行ずつ実行して、データベースが狙い通り怒ってエラー（赤バツ❌）を出すか確認してください。

-- ❌ 【複合ユニーク制約エラー】すでに登録済みの「ユーザー1の2026年3月」に対して、もう一度予算を重ねて登録しようとする
-- 想定エラーコード：1062 (Duplicate entry '1-2026-03' for key 'idx_user_month_UNIQUE')
INSERT INTO monthly_budgets (user_id, target_month, target_amount, updated_at) VALUES
(1, '2026-03', 65000, '2026-03-02 09:00:00');

-- ❌ 【NOT NULL制約エラー】target_month (対象月) を NULL にして登録しようとする
-- 想定エラーコード：1048 (Column 'target_month' cannot be null)
INSERT INTO monthly_budgets (user_id, target_month, target_amount) VALUES
(1, NULL, 50000);

-- ❌ 【データ長超過エラー】CHAR(7)の制限に対して、「2026-03-01」のように文字数が溢れる日付形式を無理やり入れようとする
-- 想定エラーコード：1406 (Data too long for column 'target_month' at row 1)
INSERT INTO monthly_budgets (user_id, target_month, target_amount) VALUES
(1, '2026-03-01', 50000);

-- ❌ 【NOT NULL制約エラー】target_amount (目標金額) を明示的に NULL にして登録しようとする
-- 想定エラーコード：1048 (Column 'target_amount' cannot be null)
INSERT INTO monthly_budgets (user_id, target_month, target_amount) VALUES
(1, '2026-06', NULL);

-- ❌ 【範囲超過エラー】目標金額に 100兆円（INTEGERの限界値 約21億 を超える数値）を入れて溢れさせる
-- 想定エラーコード：1264 (Out of range value for column 'target_amount')
INSERT INTO monthly_budgets (user_id, target_month, target_amount) VALUES
(1, '2026-06', 1000000000000000);

-- ❌ 【NOT NULL制約エラー】user_id を NULL にして登録しようとする
-- 想定エラーコード：1048 (Column 'user_id' cannot be null)
INSERT INTO monthly_budgets (user_id, target_month, target_amount) VALUES
(NULL, '2026-06', 50000);

-- ❌ 【外部キー制約エラー】usersテーブルに存在しないダミーのユーザーID：99999 で予算を登録しようとする
-- 想定エラーコード：1452 (Cannot add or update a child row: a foreign key constraint fails...)
INSERT INTO monthly_budgets (user_id, target_month, target_amount) VALUES
(99999, '2026-06', 50000);