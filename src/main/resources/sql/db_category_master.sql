-- カテゴリに関するSQL文
SELECT * FROM omakakakeibo_db.categories_master;

-- カテゴリマスタテーブル（過去履歴すべて）
CREATE TABLE categories_master (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    category_name VARCHAR(10) NOT NULL,
    color_index INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
      
	PRIMARY KEY (`category_id`),
  
	-- 外部キー制約をつけて外部テーブルと紐づけする
	CONSTRAINT `fk_category_user_id`
	FOREIGN KEY (`user_id`)
	REFERENCES `omakakakeibo_db`.`users` (`user_id`)
	ON DELETE CASCADE
);

-- =====================================================================
-- 1. 【正常系】通常の初期登録ダミーデータ（テスト成功用）
-- =====================================================================
-- 💡 ユーザーID: 1 の基本となる6つのカテゴリマスタを登録します。

-- =====================================================================
-- 1. 【正常系】通常の初期登録ダミーデータ（テスト成功用）
-- =====================================================================
-- 💡 ユーザーID: 1 の基本となる6つのカテゴリマスタを登録します。
-- ※元のデータにあった空文字「''」は、画面バグを防ぐため「その他」に変更してあります。

INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(1, '必要経費',   0), 
(1, 'ごほうび',   1),
(1, '推し活',     2),
(1, 'カフェ',     3),
(1, 'わからない', 4),
(1, '',     5);

-- =====================================================================
-- 2. 【異常系】仕様・制約をすり抜けるゴミデータを防ぐエラーテスト
-- =====================================================================
-- 💡 1行ずつ実行して、データベースが狙い通り怒ってエラー（赤バツ❌）を出すか確認してください。

-- ❌ 【文字数制限エラー】10文字制限（VARCHAR(10)）に対して、13文字の文字列を叩き込んで溢れさせる！
-- 想定エラーコード：1406 (Data too long for column 'category_name' at row 1)
INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(1, 'じゅうさんもじのなまえです', 1);

-- ❌ 【NOT NULL制約エラー】category_name を NULL (空っぽ) にして登録しようとする
-- 想定エラーコード：1048 (Column 'category_name' cannot be null)
INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(1, NULL, 2);

-- ❌ 【NOT NULL制約エラー】user_id を NULL にして登録しようとする
-- 想定エラーコード：1048 (Column 'user_id' cannot be null)
INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(NULL, 'エラーテスト', 0);

-- ❌ 【外部キー制約エラー】usersテーブルに存在しないダミーのユーザーID：99999 で登録しようとする
-- 想定エラーコード：1452 (Cannot add or update a child row: a foreign key constraint fails...)
INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(99999, 'エラーテスト', 0);

-- ❌ 【データ型エラー】数値型(INT)の color_index に、絶対に変換できない文字列 'red' を叩き込んで弾かれる！
-- 想定エラーコード：1366 (Incorrect integer value)
INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(1, 'カラーエラー', 'red');

-- ❌ 【NOT NULL制約エラー】color_index を NULL にして登録しようとする（color_indexも NOT NULL なので弾かれる！）
-- 想定エラーコード：1048 (Column 'color_index' cannot be null)
INSERT INTO categories_master (user_id, category_name, color_index) VALUES 
(1, 'カラーなし', NULL);