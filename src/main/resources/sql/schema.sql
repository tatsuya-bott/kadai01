CREATE TABLE IF NOT EXISTS `user` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `mail` VARCHAR(128) NOT NULL, -- メールアドレス
    `password` VARCHAR(64) NOT NULL, -- パスワードのハッシュ値 spring security
    `roles` VARCHAR(255) NOT NULL, -- ロール カンマ区切り文字列 "ROLE_USER,ROLE_ADMIN" spring security
    `created` DATETIME NOT NULL, -- 作成時刻
    `last_logined` DATETIME NOT NULL, -- 最終ログイン時刻
    `enabled` BOOLEAN NOT NULL, -- 有効、無効フラグ
    UNIQUE KEY (`mail`)
);

CREATE TABLE IF NOT EXISTS `record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT NOT NULL, -- ユーザーID
    `project_id` INT NOT NULL, -- プロジェクトID
    `display_image_ids` VARCHAR(64) NOT NULL, -- カンマ区切り文字列(３列:<基準画像,比較画像1,比較画像2>, 2列<比較画像1,比較画像2>)
    `question_id` INT NOT NULL, -- 質問文ID
    `answer_image_statuses` VARCHAR(64) NOT NULL, -- 回答の画像ステータス 選択された画像は1 他は0 カンマ区切り文字列(３列:<基準画像,比較画像1,比較画像2>, 2列<比較画像1,比較画像2>)
    `answer_start_time` VARCHAR(13) NOT NULL, -- 回答画面表示時刻 unixtimeミリ秒
    `answer_end_time` VARCHAR(13) NOT NULL, -- 回答終了時刻 unixtimeミリ秒
    `created` DATETIME NOT NULL -- 作成時刻
);

CREATE TABLE IF NOT EXISTS `question` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `project_id` INT NOT NULL, -- 対象プロジェクトID
    `text` VARCHAR(64) NOT NULL, -- 質問文
    `created` DATETIME NOT NULL, -- 作成時刻
    `enabled` BOOLEAN NOT NULL, -- 有効、無効フラグ
    UNIQUE KEY (`project_id`, `text`)
);

CREATE TABLE IF NOT EXISTS `image` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `question_id` INT NOT NULL, -- 質問文ID
    `file_path` VARCHAR(128) NOT NULL, -- 画像ファイルのパス
    `created` DATETIME NOT NULL, -- 作成時刻
    `enabled` BOOLEAN NOT NULL, -- 有効、無効フラグ
    UNIQUE KEY (`question_id`, `file_path`)
);

CREATE TABLE IF NOT EXISTS `project` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL, -- プロジェクト名
    `description` VARCHAR(255) NOT NULL, -- 説明
    `type` INT NOT NULL, -- タイプ(0: 比較画像２つ, 1:基準画像と比較画像２つ)
    `enabled` BOOLEAN NOT NULL -- 有効、無効フラグ
);

-- アプリケーションの設定、情報テーブル
CREATE TABLE IF NOT EXISTS `application` (
    `name` VARCHAR(64) PRIMARY KEY, -- 固定値(環境ごと local, dev, prod...)
    `record_last_updated` DATETIME -- レコードファイル作成バッチの最終更新日
);