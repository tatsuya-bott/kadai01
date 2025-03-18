INSERT IGNORE INTO `user`(id, mail, password, roles, created, last_logined, enabled) VALUES
-- adminパスワード: password
(1, 'test_user@test.jp', '$2a$10$oCj5iwYAVJ78fm8VkrOnMOG3caMrMl0t94Wc/CM8bC0DAM867gyPK', 'ROLE_USER', '2022-01-01 00:00:00', '2022-01-01 00:00:00', true),
(2, 'test_admin', '$2a$10$/bGgp.eMJ8IW1UKatZgKKuLzcfIY9eJMqgz.HlX4CJjQuLTh1ic/y', 'ROLE_ADMIN', '2022-01-01 00:00:00', '2022-01-01 00:00:00', true),
(3, 'test_admin@test.jp', '$2a$10$/bGgp.eMJ8IW1UKatZgKKuLzcfIY9eJMqgz.HlX4CJjQuLTh1ic/y', 'ROLE_ADMIN', '2022-01-01 00:00:00', '2022-01-01 00:00:00', true),
(4, 'test_user_disable@test.jp', '$2a$10$oCj5iwYAVJ78fm8VkrOnMOG3caMrMl0t94Wc/CM8bC0DAM867gyPK', 'ROLE_USER', '2022-01-01 00:00:00', '2022-01-01 00:00:00', false);

INSERT IGNORE INTO `record`(id, user_id, project_id, display_image_ids, question_id, answer_image_statuses, answer_start_time, answer_end_time, created) VALUES
(1, 1, 1, '1,2', 1, '1,0', '1680977375131', '1680978375131', '2023-04-09 03:09:40');

INSERT IGNORE INTO `question`(id, project_id, text, created, enabled) VALUES
(1, 1, '可愛い', '2022-01-01 00:00:00', true),
(2, 1, '綺麗', '2022-01-01 00:00:00', true),
(3, 2, '食欲がわく', '2022-01-01 00:00:00', true),
(4, 2, '美味しそう', '2022-01-01 00:00:00', true),
(5, 3, '先頭の画像に似ているのはどちらか', '2022-01-01 00:00:00', true),
(6, 1, '画像不足質問', '2022-01-01 00:00:00', true),
(7, 1, 'Disabled質問', '2022-01-01 00:00:00', false);

INSERT IGNORE INTO `image`(id, question_id, file_path, created, enabled) VALUES
(1, 1, 'image1.jpg', '2022-01-01 00:00:00', true),
(2, 1, 'image2.jpg', '2022-01-01 00:00:00', true),
(3, 2, 'image1.jpg', '2022-01-01 00:00:00', true),
(4, 2, 'image2.jpg', '2022-01-01 00:00:00', true),
(5, 3, 'image1.jpg', '2022-01-01 00:00:00', true),
(6, 3, 'image2.jpg', '2022-01-01 00:00:00', true),
(7, 4, 'image3.jpg', '2022-01-01 00:00:00', true),
(8, 4, 'image4.jpg', '2022-01-01 00:00:00', true),
(9, 5, 'image_base1.jpg', '2022-01-01 00:00:00', true),
(10, 5, 'image6.jpg', '2022-01-01 00:00:00', true),
(11, 5, 'image7.jpg', '2022-01-01 00:00:00', true),
(12, 6, 'image1.jpg', '2022-01-01 00:00:00', true),
(13, 6, 'image2.jpg', '2022-01-01 00:00:00', false),
(14, 7, 'image1.jpg', '2022-01-01 00:00:00', true),
(14, 8, 'image2.jpg', '2022-01-01 00:00:00', true);

INSERT IGNORE INTO `project`(id, name, description, type, enabled) VALUES
(1, '花', '比較画像', 0, true),
(2, 'ラーメン', '比較画像', 0, true),
(3, 'トリプレット', '基準画像と比較画像を表示', 1, true),
(4, 'Disabledプロジェクト', '基準画像と比較画像を表示', 1, false);
