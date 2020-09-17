/*
 Navicat Premium Data Transfer

 Source Server         : CraneEye
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : 47.103.148.156:3317
 Source Schema         : ikc

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 17/09/2020 15:30:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for operator_accredit
-- ----------------------------
DROP TABLE IF EXISTS `operator_accredit`;
CREATE TABLE `operator_accredit` (
  `tenant_id` bigint(20) NOT NULL,
  `object_type` int(11) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tenant_id`,`object_type`,`object_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_branch
-- ----------------------------
DROP TABLE IF EXISTS `operator_branch`;
CREATE TABLE `operator_branch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_duty
-- ----------------------------
DROP TABLE IF EXISTS `operator_duty`;
CREATE TABLE `operator_duty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_privilege
-- ----------------------------
DROP TABLE IF EXISTS `operator_privilege`;
CREATE TABLE `operator_privilege` (
  `tenant_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `code` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`tenant_id`,`role_id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_role
-- ----------------------------
DROP TABLE IF EXISTS `operator_role`;
CREATE TABLE `operator_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_user
-- ----------------------------
DROP TABLE IF EXISTS `operator_user`;
CREATE TABLE `operator_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `username` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `branch_id` bigint(20) DEFAULT NULL,
  `duty_id` bigint(20) DEFAULT NULL,
  `nickname` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `gender` enum('unknown','male','female') COLLATE utf8mb4_bin DEFAULT 'unknown',
  `title` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `avatar` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `postcode` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `company` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `last_login` date DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for operator_user_data
-- ----------------------------
DROP TABLE IF EXISTS `operator_user_data`;
CREATE TABLE `operator_user_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_files
-- ----------------------------
DROP TABLE IF EXISTS `system_files`;
CREATE TABLE `system_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `filename` varchar(500) DEFAULT NULL,
  `local_path` varchar(2000) NOT NULL,
  `remote_url` varchar(2000) NOT NULL,
  `purpose` varchar(200) DEFAULT NULL,
  `content_type` varchar(50) DEFAULT '',
  `file_size` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_logging
-- ----------------------------
DROP TABLE IF EXISTS `system_logging`;
CREATE TABLE `system_logging` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `username` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `module` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `method` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `primary_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `primary_info` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `detail` mediumtext COLLATE utf8mb4_bin,
  `result` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_push
-- ----------------------------
DROP TABLE IF EXISTS `system_push`;
CREATE TABLE `system_push` (
  `user_id` bigint(20) NOT NULL,
  `client_id` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `push_channel` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `message_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_setting
-- ----------------------------
DROP TABLE IF EXISTS `system_setting`;
CREATE TABLE `system_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `module` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `key` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `value` mediumtext COLLATE utf8mb4_bin,
  `show_value` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_sms
-- ----------------------------
DROP TABLE IF EXISTS `system_sms`;
CREATE TABLE `system_sms` (
  `sms_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `mobile` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `action` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `content` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `result` tinyint(4) NOT NULL DEFAULT '1',
  `message` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `send_time` datetime NOT NULL,
  PRIMARY KEY (`sms_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_tenant
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant`;
CREATE TABLE `system_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `orgin_code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `modules` varchar(10240) COLLATE utf8mb4_bin DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `template` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for system_tenant_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant_menu`;
CREATE TABLE `system_tenant_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `group` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL,
  `remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  `sort` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
