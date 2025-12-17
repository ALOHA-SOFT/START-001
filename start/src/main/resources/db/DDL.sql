-- Active: 1765956701887@@127.0.0.1@3306@start

CREATE DATABASE IF NOT EXISTS `start`
    DEFAULT CHARACTER SET = 'utf8mb4';

USE `start`;

-- 외래키 무시
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `code_groups`;
CREATE TABLE `code_groups` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(100) NOT NULL COMMENT 'UK',
  `name` varchar(100) NOT NULL COMMENT '코드그룸명',
  `description` text NOT NULL COMMENT '설명',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `codes`;
CREATE TABLE `codes` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(100) NOT NULL COMMENT 'UK',
  `code_group_no` bigint NOT NULL COMMENT 'FK',
  `name` varchar(100) NOT NULL COMMENT '코드명',
  `value` varchar(100) NOT NULL COMMENT '코드 값',
  `code` varchar(100) DEFAULT NULL COMMENT '업무코드',
  `description` text NOT NULL COMMENT '설명',
  `seq` int NOT NULL DEFAULT '0' COMMENT '순서',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`),
  UNIQUE KEY `id` (`id`),
  KEY `code_group_no` (`code_group_no`),
  CONSTRAINT `codes_ibfk_1` FOREIGN KEY (`code_group_no`) REFERENCES `code_groups` (`no`) ON DELETE CASCADE ON UPDATE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;



DROP TABLE IF EXISTS `sample`;
CREATE TABLE `sample` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(64) NOT NULL COMMENT 'UK',
  `name` varchar(100) NOT NULL COMMENT '이름',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`),
  UNIQUE KEY `id` (`id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `seq_groups`;
CREATE TABLE `seq_groups` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(100) NOT NULL COMMENT 'UK',
  `code` varchar(100) NOT NULL COMMENT '시퀀스를 식별하는 코드',
  `name` varchar(100) NOT NULL COMMENT '시퀀스 이름',
  `value` bigint NOT NULL DEFAULT '0' COMMENT '그룹 누적 시퀀스 번호',
  `step` bigint NOT NULL DEFAULT '1' COMMENT '증감치 (기본적으로 +1)',
  `description` text COMMENT '설명',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`),
  UNIQUE KEY `id` (`id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `seq`;
CREATE TABLE `seq` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(100) NOT NULL COMMENT 'UK',
  `seq_group_no` bigint NOT NULL COMMENT 'FK',
  `code` varchar(100) NOT NULL COMMENT '시퀀스를 식별하는 코드 (seq_group_code)',
  `value` bigint NOT NULL DEFAULT '0' COMMENT '현재 시퀀스 번호',
  `date` date NOT NULL COMMENT '적용일자 (YYYY-MM-DD)',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`),
  UNIQUE KEY `id` (`id`),
  KEY `seq_group_no` (`seq_group_no`),
  CONSTRAINT `seq_ibfk_1` FOREIGN KEY (`seq_group_no`) REFERENCES `seq_groups` (`no`) ON DELETE CASCADE ON UPDATE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(64) NOT NULL COMMENT 'UK',
  `username` varchar(100) NOT NULL COMMENT '아이디',
  `password` varchar(100) NOT NULL COMMENT '비밀번호',
  `name` varchar(100) NOT NULL COMMENT '이름',
  `tel` varchar(100) NOT NULL COMMENT '전화번호',
  `email` varchar(100) NOT NULL COMMENT '이메일',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '사용여부',
  `note` text DEFAULT NULL COMMENT '비고',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;



DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth` (
  `no` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(64) NOT NULL COMMENT 'UK',
  `user_no` bigint NOT NULL COMMENT 'FK',
  `username` varchar(100) NOT NULL COMMENT '아이디',
  `auth` varchar(100) NOT NULL COMMENT '권한',
  `name` varchar(100) NOT NULL COMMENT '이름',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`no`),
  UNIQUE KEY `id` (`id`),
  KEY `user_no` (`user_no`),
  CONSTRAINT `user_auth_ibfk_1` FOREIGN KEY (`user_no`) REFERENCES `users` (`no`) ON DELETE CASCADE ON UPDATE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


-- 외래키 복원
SET FOREIGN_KEY_CHECKS = 1;