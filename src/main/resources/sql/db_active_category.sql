-- アクティブカテゴリに関するSQL文
SELECT * FROM omakakakeibo_db.active_categories;

-- アクティブカテゴリテーブル（現在の設定枠）
CREATE TABLE active_categories (
    active_cat_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    category_id BIGINT NOT NULL,
    slot_number INT NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`active_cat_id`),
    
	-- 1人のユーザーは、1つのスロット番号に1データしか持てない
    -- （例：Aさんの1番目の枠には1つのカテゴリしか絶対に登録できない ➔ これで6個固定が物理的に確定します）
    UNIQUE INDEX `idx_user_slot_UNIQUE` (`user_id` ASC, `slot_number` ASC) VISIBLE,
    
    -- 1人のユーザーが、同じカテゴリを複数の枠に使い回すのを禁止する
    UNIQUE INDEX `idx_user_category_UNIQUE` (`user_id` ASC, `category_id` ASC) VISIBLE,
    
    -- slot_number は 1 から 6 の間しか絶対に許さない
    CONSTRAINT `chk_slot_range` CHECK (slot_number BETWEEN 1 AND 6),
    
	-- ユーザーテーブルとの紐付け（退会したら連動削除）
    CONSTRAINT `fk_active_cat_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `omakakakeibo_db`.`users` (`user_id`)
    ON DELETE CASCADE,
    
    -- マスタテーブルとの紐付け（マスタから消えたらここからも消える）
    CONSTRAINT `fk_active_cat_category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `omakakakeibo_db`.`categories_master` (`category_id`)
    ON DELETE CASCADE
);


-- =====================================================================
-- 1. 【正常系】通常の初期登録ダミーデータ（テスト成功用）
-- =====================================================================
-- 💡 ユーザーID: 1 の通常のカテゴリマスタと、それを6つの枠（スロット）に綺麗にハメる正常なデータです。
-- (※一度テーブルの中身を空にするか、IDが被らないようにテストしてください)

-- active_categories への正常登録 (1人6枠を綺麗に埋める)
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES 
(1, 1, 1),
(1, 2, 2),
(1, 3, 3),
(1, 4, 4),
(1, 5, 5),
(1, 6, 6);


-- =====================================================================
-- 2. 【フロントテスト用】「過去（4月）に変更された」ことにするダミーデータ
-- =====================================================================
-- 💡 これを流すと、データの最終更新日が「2026年4月」になります。
-- 現実の「今月（6月）」とは月がズレるため、フロントのJS判定（checkAlreadyEditCategory）が
-- 正常に「true（編集可能！）」を返し、ボタンのロックがパッと解除されるテストができます！

-- ② active_categories の更新（updated_at を 4月 に明示的に指定して枠にハメる）
INSERT INTO active_categories (user_id, category_id, slot_number, updated_at) VALUES 
(1, 1, 1, '2026-04-15 10:00:00'),
(1, 2, 2, '2026-04-15 10:00:00'),
(1, 3, 3, '2026-04-15 10:00:00'),
(1, 4, 4, '2026-04-15 10:00:00'),
(1, 5, 5, '2026-04-15 10:00:00'),
(1, 6, 6, '2026-04-15 10:00:00');


-- =====================================================================
-- 3. 【異常系】仕様・制約をすり抜けるゴミデータを防ぐエラーテスト
-- =====================================================================
-- 💡 1行ずつ実行して、データベースが狙い通り怒ってエラー（赤バツ❌）を出すか確認してください。

-- ❌ 【NOT NULL制約エラー】user_id を NULL にして登録しようとする
-- 想定エラーコード：1048 (Column 'user_id' cannot be null)
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES (NULL, 1, 1);

-- ❌ 【NOT NULL制約エラー】slot_number (枠番号) を NULL にして登録しようとする
-- 想定エラーコード：1048 (Column 'slot_number' cannot be null)
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES (1, 1, NULL);

-- ❌ 【枠番号（スロット）の範囲エラー】CHECK制約（スロットは1〜6限定）により、7番を入れようとして弾かれる！
-- 想定エラーコード：3819 (Check constraint '...' is violated.)
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES (1, 1, 7);

-- ❌ 【スロットの重複エラー（409エラー）】1人6枠を固定するため、すでに埋まっている1番目の枠に別のカテゴリを重ねようとしてユニークエラー！
-- 想定エラーコード：1062 (Duplicate entry '1-1' for key '...※user_idとslot_numberの複合ユニーク')
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES (1, 2, 1);

-- ❌ 【カテゴリの使い回し禁止エラー】1人のユーザーが、複数のスロット枠に同じ「カテゴリID: 1」を使い回しセットするのを防ぐユニークエラー！
-- 想定エラーコード：1062 (Duplicate entry '1-1' for key '...※user_idとcategory_idの複合ユニーク')
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES (1, 1, 2);

-- ❌ 【データ型エラー】数値型の slot_number に文字 'first' を叩き込んで弾かれる
-- 想定エラーコード：1366 (Incorrect integer value)
INSERT INTO active_categories (user_id, category_id, slot_number) VALUES (1, 1, 'first');