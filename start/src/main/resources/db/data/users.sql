-- 외래키 체크 비활성화
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `user_auth`;
TRUNCATE TABLE `users`;

-- users 샘플 데이터
INSERT INTO `users` (`id`, `username`, `password`, `name`, `tel`, `email`, `enabled`) VALUES
(UUID(), 'user', '$2a$10$fKQnGL./xuKkevPl99Y1yu2092.YF8drw.A9ZiMLe5tnJuXIs99BK', '일반사용자', '010-1111-1111', 'user@example.com', 1),
(UUID(), 'aloha', '$2a$10$fKQnGL./xuKkevPl99Y1yu2092.YF8drw.A9ZiMLe5tnJuXIs99BK', '알로하', '010-2222-2222', 'aloha@example.com', 1),
(UUID(), 'admin', '$2a$10$fKQnGL./xuKkevPl99Y1yu2092.YF8drw.A9ZiMLe5tnJuXIs99BK', '관리자', '010-9999-9999', 'admin@example.com', 1);

-- user_auth 샘플 데이터
INSERT INTO `user_auth` (`id`, `user_no`, `username`, `auth`, `name`) 
SELECT UUID(), u.no, u.username, 'ROLE_USER', u.name FROM `users` u WHERE u.username = 'user';

INSERT INTO `user_auth` (`id`, `user_no`, `username`, `auth`, `name`) 
SELECT UUID(), u.no, u.username, 'ROLE_USER', u.name FROM `users` u WHERE u.username = 'aloha';

INSERT INTO `user_auth` (`id`, `user_no`, `username`, `auth`, `name`) 
SELECT UUID(), u.no, u.username, 'ROLE_USER', u.name FROM `users` u WHERE u.username = 'admin';

INSERT INTO `user_auth` (`id`, `user_no`, `username`, `auth`, `name`) 
SELECT UUID(), u.no, u.username, 'ROLE_ADMIN', u.name FROM `users` u WHERE u.username = 'admin';

-- 외래키 체크 활성화
SET FOREIGN_KEY_CHECKS = 1;



