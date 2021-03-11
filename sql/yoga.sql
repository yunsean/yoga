/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : yoga

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 11/03/2021 10:34:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for moment_follow
-- ----------------------------
DROP TABLE IF EXISTS `moment_follow`;
CREATE TABLE `moment_follow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message_id` bigint NOT NULL,
  `creator_id` bigint NOT NULL,
  `replier_id` bigint NOT NULL,
  `reply_time` datetime NOT NULL,
  `content` text,
  `receiver_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `moment_id` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for moment_group
-- ----------------------------
DROP TABLE IF EXISTS `moment_group`;
CREATE TABLE `moment_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for moment_group_user
-- ----------------------------
DROP TABLE IF EXISTS `moment_group_user`;
CREATE TABLE `moment_group_user` (
  `group_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for moment_message
-- ----------------------------
DROP TABLE IF EXISTS `moment_message`;
CREATE TABLE `moment_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `group_id` bigint NOT NULL DEFAULT '0',
  `creator_id` bigint NOT NULL,
  `issue_time` datetime NOT NULL,
  `content` text,
  `image_ids` varchar(2000) DEFAULT NULL,
  `image_urls` text,
  `file_ids` varchar(2000) DEFAULT NULL,
  `file_names` text,
  `link_url` varchar(2000) DEFAULT NULL,
  `link_title` varchar(200) DEFAULT NULL,
  `link_poster` varchar(2000) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `poi_id` varchar(200) DEFAULT NULL,
  `poi_title` varchar(2000) DEFAULT NULL,
  `ad_url` varchar(2000) DEFAULT NULL,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for moment_upvote
-- ----------------------------
DROP TABLE IF EXISTS `moment_upvote`;
CREATE TABLE `moment_upvote` (
  `message_id` bigint NOT NULL,
  `upvoter_id` bigint NOT NULL,
  `issue_time` datetime NOT NULL,
  PRIMARY KEY (`message_id`,`upvoter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for operator_accredit
-- ----------------------------
DROP TABLE IF EXISTS `operator_accredit`;
CREATE TABLE `operator_accredit` (
  `tenant_id` bigint NOT NULL,
  `object_type` int NOT NULL,
  `object_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`tenant_id`,`object_type`,`object_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_branch
-- ----------------------------
DROP TABLE IF EXISTS `operator_branch`;
CREATE TABLE `operator_branch` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parent_id` bigint NOT NULL DEFAULT '0',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_duty
-- ----------------------------
DROP TABLE IF EXISTS `operator_duty`;
CREATE TABLE `operator_duty` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `level` int NOT NULL DEFAULT '0',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_privilege
-- ----------------------------
DROP TABLE IF EXISTS `operator_privilege`;
CREATE TABLE `operator_privilege` (
  `tenant_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`tenant_id`,`role_id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_role
-- ----------------------------
DROP TABLE IF EXISTS `operator_role`;
CREATE TABLE `operator_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_user
-- ----------------------------
DROP TABLE IF EXISTS `operator_user`;
CREATE TABLE `operator_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `branch_id` bigint DEFAULT NULL,
  `duty_id` bigint DEFAULT NULL,
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `gender` enum('unknown','male','female') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'unknown',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `postcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `last_login` date DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `enabled` tinyint NOT NULL DEFAULT '1',
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_user_data
-- ----------------------------
DROP TABLE IF EXISTS `operator_user_data`;
CREATE TABLE `operator_user_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for points_adjust
-- ----------------------------
DROP TABLE IF EXISTS `points_adjust`;
CREATE TABLE `points_adjust` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `add_time` datetime NOT NULL,
  `points` int NOT NULL,
  `reason` varchar(1000) NOT NULL,
  `submitter_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for points_summary
-- ----------------------------
DROP TABLE IF EXISTS `points_summary`;
CREATE TABLE `points_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `year` int NOT NULL,
  `points` int NOT NULL,
  `year_rank` int NOT NULL DEFAULT '0',
  `detail` varchar(10240) DEFAULT NULL,
  `penalty` int NOT NULL DEFAULT '0',
  `duty_rank` int NOT NULL DEFAULT '0',
  `branch_rank` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_user_year` (`tenant_id`,`user_id`,`year`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for points_year
-- ----------------------------
DROP TABLE IF EXISTS `points_year`;
CREATE TABLE `points_year` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `year` int NOT NULL,
  `begin_date` date NOT NULL,
  `end_date` date NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `warning_user_ids` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenantId_year` (`tenant_id`,`year`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for system_files
-- ----------------------------
DROP TABLE IF EXISTS `system_files`;
CREATE TABLE `system_files` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `filename` varchar(500) DEFAULT NULL,
  `local_path` varchar(2000) NOT NULL,
  `remote_url` varchar(2000) NOT NULL,
  `purpose` varchar(200) DEFAULT NULL,
  `content_type` varchar(50) DEFAULT '',
  `file_size` bigint NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_logging
-- ----------------------------
DROP TABLE IF EXISTS `system_logging`;
CREATE TABLE `system_logging` (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `create_time` datetime NOT NULL,
  `user_id` bigint NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `method` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `primary_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `primary_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `detail` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_push
-- ----------------------------
DROP TABLE IF EXISTS `system_push`;
CREATE TABLE `system_push` (
  `user_id` bigint NOT NULL,
  `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `push_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `message_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_setting
-- ----------------------------
DROP TABLE IF EXISTS `system_setting`;
CREATE TABLE `system_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `value` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `show_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_sms
-- ----------------------------
DROP TABLE IF EXISTS `system_sms`;
CREATE TABLE `system_sms` (
  `sms_id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `result` tinyint NOT NULL DEFAULT '1',
  `message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `send_time` datetime NOT NULL,
  PRIMARY KEY (`sms_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_tenant
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant`;
CREATE TABLE `system_tenant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `orgin_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `modules` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `template_id` bigint DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `template` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_tenant_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant_menu`;
CREATE TABLE `system_tenant_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `group` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `enabled` tinyint NOT NULL DEFAULT '1',
  `sort` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for wxmp_bind_user
-- ----------------------------
DROP TABLE IF EXISTS `wxmp_bind_user`;
CREATE TABLE `wxmp_bind_user` (
  `openid` varchar(50) NOT NULL,
  `tenant_id` bigint NOT NULL,
  `user_id` bigint NOT NULL DEFAULT '101',
  PRIMARY KEY (`openid`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for wxmp_send_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `wxmp_send_subscribe`;
CREATE TABLE `wxmp_send_subscribe` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `template_id` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `page` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `data` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `send` tinyint NOT NULL DEFAULT '0',
  `send_time` datetime DEFAULT NULL,
  `message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
