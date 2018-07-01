/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost
 Source Database       : ewedding

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : utf-8

 Date: 06/30/2018 20:08:48 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `cms_column`
-- ----------------------------
DROP TABLE IF EXISTS `cms_column`;
CREATE TABLE `cms_column` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `is_enable` tinyint(4) NOT NULL,
  `template_id` int(11) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COLUMN_TEMPLATE_ID` (`template_id`),
  CONSTRAINT `FK_COLUMN_TEMPLATE_ID` FOREIGN KEY (`template_id`) REFERENCES `cms_template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_layout`
-- ----------------------------
DROP TABLE IF EXISTS `cms_layout`;
CREATE TABLE `cms_layout` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `template_id` bigint(11) NOT NULL,
  `type` enum('LIST','DETAIL') COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `image` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `fields` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `html` text COLLATE utf8_bin,
  `css` text COLLATE utf8_bin,
  `js` text COLLATE utf8_bin,
  `link_files` text COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `cms_property`
-- ----------------------------
DROP TABLE IF EXISTS `cms_property`;
CREATE TABLE `cms_property` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `parent_id` int(11) NOT NULL DEFAULT '0',
  `code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_template`
-- ----------------------------
DROP TABLE IF EXISTS `cms_template`;
CREATE TABLE `cms_template` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `is_enable` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `cms_template_field`
-- ----------------------------
DROP TABLE IF EXISTS `cms_template_field`;
CREATE TABLE `cms_template_field` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `template_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` int(11) NOT NULL,
  `param` varchar(512) DEFAULT NULL,
  `hint` varchar(50) NOT NULL,
  `is_enable` tinyint(4) NOT NULL,
  `code` varchar(50) NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `placeholder` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_FIELD_TEMPLATE_ID` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ew_charge`
-- ----------------------------
DROP TABLE IF EXISTS `ew_charge`;
CREATE TABLE `ew_charge` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  `original_fee` double NOT NULL,
  `monthly_fee` double NOT NULL,
  `quarterly_fee` double NOT NULL,
  `halfyear_fee` double NOT NULL,
  `yearly_fee` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_id` (`tenant_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ew_counselor`
-- ----------------------------
DROP TABLE IF EXISTS `ew_counselor`;
CREATE TABLE `ew_counselor` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `status` enum('filling','checking','rejected','accepted') NOT NULL,
  `pid` varchar(100) DEFAULT NULL,
  `pid_front` varchar(2000) DEFAULT NULL,
  `pid_back` varchar(2000) DEFAULT NULL,
  `images` varchar(10000) DEFAULT NULL,
  `reject_reason` varchar(2000) DEFAULT NULL,
  `prove_token` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `nation` varchar(100) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `address` varchar(1024) DEFAULT NULL,
  `expire` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ew_recharge`
-- ----------------------------
DROP TABLE IF EXISTS `ew_recharge`;
CREATE TABLE `ew_recharge` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `amount` double NOT NULL,
  `order_no` varchar(100) NOT NULL,
  `trade_no` varchar(200) DEFAULT NULL,
  `seller_id` varchar(200) DEFAULT NULL,
  `status` enum('pay','paied','refund','closed') NOT NULL DEFAULT 'pay',
  `refund_time` datetime DEFAULT NULL,
  `refund_actor` bigint(20) NOT NULL DEFAULT '0',
  `expire_to` date NOT NULL,
  `recharge_type` enum('monthly','quarterly','halfyear','yearly') NOT NULL DEFAULT 'monthly',
  PRIMARY KEY (`id`),
  KEY `order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `g_files`
-- ----------------------------
DROP TABLE IF EXISTS `g_files`;
CREATE TABLE `g_files` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `add_time` datetime NOT NULL,
  `local_path` varchar(2000) NOT NULL,
  `remote_url` varchar(2000) NOT NULL,
  `purpose` varchar(200) DEFAULT NULL,
  `file_size` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `g_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `g_sequence`;
CREATE TABLE `g_sequence` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT 'sequence名称',
  `max` bigint(11) NOT NULL DEFAULT '1' COMMENT '最大id',
  `length` int(11) NOT NULL DEFAULT '12' COMMENT '生成序列后的长度,以0补全',
  `next` int(11) NOT NULL DEFAULT '1' COMMENT '增长的长度',
  `rules` varchar(255) DEFAULT NULL COMMENT '规则以###max_id###做为替换',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='整个系统表的序号';

-- ----------------------------
--  Table structure for `g_tenant`
-- ----------------------------
DROP TABLE IF EXISTS `g_tenant`;
CREATE TABLE `g_tenant` (
  `id` int(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `orgin_code` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `modules` varchar(1024) NOT NULL,
  `create_time` datetime NOT NULL,
  `template_id` bigint(20) NOT NULL DEFAULT '0',
  `expire_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `g_tenant_menu`
-- ----------------------------
DROP TABLE IF EXISTS `g_tenant_menu`;
CREATE TABLE `g_tenant_menu` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `menu_code` varchar(100) NOT NULL,
  `menu_group` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `url` varchar(1024) NOT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  `disabled` tinyint(4) NOT NULL DEFAULT '0',
  `sort` int(11) NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `g_tenant_setting`
-- ----------------------------
DROP TABLE IF EXISTS `g_tenant_setting`;
CREATE TABLE `g_tenant_setting` (
  `id` int(11) NOT NULL,
  `template_id` bigint(20) NOT NULL,
  `key_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '键',
  `key_value` varchar(20480) CHARACTER SET utf8 DEFAULT NULL COMMENT '值',
  `module` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '模块',
  `show_value` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '值显示内容',
  `system_only` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `g_tenant_template`
-- ----------------------------
DROP TABLE IF EXISTS `g_tenant_template`;
CREATE TABLE `g_tenant_template` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  `modules` varchar(1024) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_friend`
-- ----------------------------
DROP TABLE IF EXISTS `im_friend`;
CREATE TABLE `im_friend` (
  `user_id` bigint(20) NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `add_time` datetime NOT NULL,
  `allow_moment` tinyint(4) NOT NULL DEFAULT '1',
  `accepted` tinyint(4) NOT NULL DEFAULT '0',
  `blocked` tinyint(4) NOT NULL DEFAULT '0',
  `alias` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_group`
-- ----------------------------
DROP TABLE IF EXISTS `im_group`;
CREATE TABLE `im_group` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `avatar` varchar(1024) DEFAULT NULL,
  `member_count` int(11) NOT NULL DEFAULT '0',
  `creator_id` bigint(20) NOT NULL,
  `creator` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_group_user`
-- ----------------------------
DROP TABLE IF EXISTS `im_group_user`;
CREATE TABLE `im_group_user` (
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_type` enum('NORMAL','ADMIN') DEFAULT 'NORMAL',
  `applying` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_moment`
-- ----------------------------
DROP TABLE IF EXISTS `im_moment`;
CREATE TABLE `im_moment` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `creator_id` bigint(20) NOT NULL,
  `issue_time` datetime NOT NULL,
  `content` text,
  `image_ids` varchar(2000) DEFAULT NULL,
  `image_urls` text,
  `link_url` varchar(2000) DEFAULT NULL,
  `link_title` varchar(200) DEFAULT NULL,
  `link_poster` varchar(2000) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `poi_id` varchar(200) DEFAULT NULL,
  `poi_title` varchar(2000) DEFAULT NULL,
  `ad_url` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_moment_follow`
-- ----------------------------
DROP TABLE IF EXISTS `im_moment_follow`;
CREATE TABLE `im_moment_follow` (
  `id` bigint(20) NOT NULL,
  `moment_id` bigint(20) NOT NULL,
  `creator_id` bigint(20) NOT NULL,
  `replier_id` bigint(20) NOT NULL,
  `reply_time` datetime NOT NULL,
  `content` text,
  `receiver_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `moment_id` (`moment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_moment_upvote`
-- ----------------------------
DROP TABLE IF EXISTS `im_moment_upvote`;
CREATE TABLE `im_moment_upvote` (
  `moment_id` bigint(20) NOT NULL,
  `upvoter_id` bigint(20) NOT NULL,
  `issue_time` datetime NOT NULL,
  PRIMARY KEY (`moment_id`,`upvoter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `im_user`
-- ----------------------------
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `token` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) NOT NULL,
  `avatar` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `pay_water_ali_transfer`
-- ----------------------------
DROP TABLE IF EXISTS `pay_water_ali_transfer`;
CREATE TABLE `pay_water_ali_transfer` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `order_id` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `out_biz_no` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `pay_water_alipay`
-- ----------------------------
DROP TABLE IF EXISTS `pay_water_alipay`;
CREATE TABLE `pay_water_alipay` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `type` enum('pay','refund','cancel','close') NOT NULL DEFAULT 'pay',
  `return_time` datetime NOT NULL,
  `order_no` varchar(200) NOT NULL,
  `trade_no` varchar(200) NOT NULL,
  `buyer` varchar(200) NOT NULL,
  `status` varchar(100) NOT NULL,
  `response` text NOT NULL,
  `rsa_checked` tinyint(4) NOT NULL,
  `biz_result` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `pay_water_wxpay`
-- ----------------------------
DROP TABLE IF EXISTS `pay_water_wxpay`;
CREATE TABLE `pay_water_wxpay` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_no` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trade_no` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cash_fee` int(11) DEFAULT NULL,
  `fee_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `total_fee` int(11) DEFAULT NULL,
  `is_subscribe` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `open_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `time_end` datetime DEFAULT NULL,
  `trade_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `s_accredit`
-- ----------------------------
DROP TABLE IF EXISTS `s_accredit`;
CREATE TABLE `s_accredit` (
  `tenant_id` bigint(20) NOT NULL DEFAULT '0',
  `object_type` int(11) NOT NULL COMMENT '授权对象：1部门|2职务|3用户',
  `object_id` bigint(20) NOT NULL COMMENT '授权对象ID',
  `role_id` bigint(20) NOT NULL COMMENT '赋予角色ID',
  PRIMARY KEY (`tenant_id`,`object_type`,`object_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_department`
-- ----------------------------
DROP TABLE IF EXISTS `s_department`;
CREATE TABLE `s_department` (
  `id` bigint(12) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(40) DEFAULT NULL COMMENT '部门名称',
  `remark` varchar(100) NOT NULL COMMENT '描述',
  `parent_id` bigint(12) DEFAULT '0' COMMENT '上级部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`,`remark`),
  UNIQUE KEY `tenant_id` (`tenant_id`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_duty`
-- ----------------------------
DROP TABLE IF EXISTS `s_duty`;
CREATE TABLE `s_duty` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_favor`
-- ----------------------------
DROP TABLE IF EXISTS `s_favor`;
CREATE TABLE `s_favor` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `object_id` varchar(64) NOT NULL,
  `favor_date` datetime NOT NULL,
  `title` varchar(256) NOT NULL,
  `param1` varchar(50) DEFAULT NULL,
  `param2` bigint(20) DEFAULT NULL,
  `param3` varchar(200) DEFAULT NULL,
  `param4` varchar(2000) DEFAULT NULL,
  `param5` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_privilege`
-- ----------------------------
DROP TABLE IF EXISTS `s_privilege`;
CREATE TABLE `s_privilege` (
  `code` varchar(50) NOT NULL COMMENT '权限代码',
  `role_id` bigint(12) NOT NULL COMMENT '角色Id',
  `tenant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`code`,`role_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `s_role`
-- ----------------------------
DROP TABLE IF EXISTS `s_role`;
CREATE TABLE `s_role` (
  `id` bigint(10) NOT NULL,
  `tenant_id` bigint(20) NOT NULL DEFAULT '0',
  `code` varchar(20) DEFAULT NULL COMMENT '角色代码',
  `name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `disabled` int(1) DEFAULT '1' COMMENT '是否可用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_score_adjust`
-- ----------------------------
DROP TABLE IF EXISTS `s_score_adjust`;
CREATE TABLE `s_score_adjust` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `score` int(11) NOT NULL,
  `reason` varchar(1000) NOT NULL,
  `submitter_id` bigint(20) NOT NULL,
  `submitter` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_score_summary`
-- ----------------------------
DROP TABLE IF EXISTS `s_score_summary`;
CREATE TABLE `s_score_summary` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `year` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `rank` int(11) NOT NULL DEFAULT '0',
  `detail` varchar(10240) DEFAULT NULL,
  `penalty` int(11) NOT NULL DEFAULT '0',
  `duty_rank` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_user_year` (`tenant_id`,`user_id`,`year`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_score_year`
-- ----------------------------
DROP TABLE IF EXISTS `s_score_year`;
CREATE TABLE `s_score_year` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `year` int(11) NOT NULL,
  `begin_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `warning_user_ids` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenantId_year` (`tenant_id`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_setting`
-- ----------------------------
DROP TABLE IF EXISTS `s_setting`;
CREATE TABLE `s_setting` (
  `id` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `key_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '键',
  `key_value` varchar(20480) CHARACTER SET utf8 DEFAULT NULL COMMENT '值',
  `module` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '模块',
  `show_value` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '值显示内容',
  `system_only` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `s_user`
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user` (
  `id` bigint(12) NOT NULL,
  `tenant_id` bigint(20) NOT NULL DEFAULT '0',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `dept_id` bigint(12) DEFAULT NULL COMMENT '所属部门Id',
  `duty_id` bigint(20) DEFAULT NULL,
  `lastname` varchar(10) DEFAULT NULL COMMENT '姓',
  `firstname` varchar(10) DEFAULT NULL COMMENT '名',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(20) DEFAULT NULL COMMENT 'qq',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `disabled` int(1) NOT NULL DEFAULT '0' COMMENT '是否可用',
  `fullname` varchar(20) DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  `address` varchar(512) DEFAULT NULL,
  `postcode` varchar(20) DEFAULT NULL,
  `company` varchar(200) DEFAULT NULL,
  `wechat` varchar(50) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `ext_long` bigint(20) DEFAULT NULL,
  `ext_text` varchar(2000) DEFAULT NULL,
  `ext_int` int(11) DEFAULT NULL,
  `ext_double` double DEFAULT NULL,
  `expire` date DEFAULT NULL,
  `gender` enum('unknown','male','female') DEFAULT 'unknown',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_username` (`tenant_id`,`username`) USING BTREE,
  KEY `id` (`id`),
  KEY `dept_id` (`dept_id`),
  KEY `fullname` (`fullname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `s_user_data`
-- ----------------------------
DROP TABLE IF EXISTS `s_user_data`;
CREATE TABLE `s_user_data` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sms_history`
-- ----------------------------
DROP TABLE IF EXISTS `sms_history`;
CREATE TABLE `sms_history` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `mobile` varchar(50) NOT NULL,
  `action` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `u_journal`
-- ----------------------------
DROP TABLE IF EXISTS `u_journal`;
CREATE TABLE `u_journal` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `time` datetime NOT NULL,
  `module` varchar(255) NOT NULL,
  `method` varchar(1024) DEFAULT NULL,
  `action` varchar(100) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `user` varchar(100) DEFAULT NULL,
  `detail` varchar(10240) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_account`
-- ----------------------------
DROP TABLE IF EXISTS `wx_account`;
CREATE TABLE `wx_account` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `token` varchar(200) NOT NULL,
  `number` varchar(200) NOT NULL,
  `raw_id` varchar(200) NOT NULL,
  `app_id` varchar(200) NOT NULL,
  `app_secret` varchar(200) NOT NULL,
  `aes_key` varchar(200) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `access_token` varchar(1000) DEFAULT NULL,
  `token_expire` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_action`
-- ----------------------------
DROP TABLE IF EXISTS `wx_action`;
CREATE TABLE `wx_action` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `event_key` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `action` enum('text','html','plugin') COLLATE utf8mb4_unicode_ci NOT NULL,
  `plugin_code` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `params` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `wx_article`
-- ----------------------------
DROP TABLE IF EXISTS `wx_article`;
CREATE TABLE `wx_article` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `batch_index` bigint(20) NOT NULL,
  `material_id` bigint(20) NOT NULL,
  `title` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `author` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `thumb_url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `thumb_media_id` varchar(200) CHARACTER SET utf8 NOT NULL,
  `digest` varchar(200) DEFAULT NULL,
  `content` mediumtext,
  `source_url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `show_conver_image` tinyint(4) DEFAULT '1',
  `click_url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `wx_material`
-- ----------------------------
DROP TABLE IF EXISTS `wx_material`;
CREATE TABLE `wx_material` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `batch_index` bigint(20) NOT NULL,
  `type` enum('image','voice','video','thumb','file','news') NOT NULL,
  `media_id` varchar(200) NOT NULL,
  `update_time` datetime NOT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `file` varchar(2000) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `intro` varchar(200) DEFAULT NULL,
  `is_uploaded` tinyint(4) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_material_ex`
-- ----------------------------
DROP TABLE IF EXISTS `wx_material_ex`;
CREATE TABLE `wx_material_ex` (
  `tenant_id` bigint(20) NOT NULL,
  `media_id` varchar(200) NOT NULL,
  `group_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(200) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`tenant_id`,`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_material_group`
-- ----------------------------
DROP TABLE IF EXISTS `wx_material_group`;
CREATE TABLE `wx_material_group` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_menu`
-- ----------------------------
DROP TABLE IF EXISTS `wx_menu`;
CREATE TABLE `wx_menu` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `wx_menu_id` varchar(100) DEFAULT NULL,
  `tag_id` varchar(100) DEFAULT NULL,
  `gender` enum('all','male','female') DEFAULT 'all',
  `country` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `client_os` enum('all','android','ios','other') DEFAULT 'all',
  `language` enum('all','zh_CN','zh_TW','zh_HK','en','id','ms','es','ko','it','ja','pl','pt','ru','th','vi','ar','hi','he','tr','de','fr') DEFAULT 'all',
  `is_default` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_menu_entity`
-- ----------------------------
DROP TABLE IF EXISTS `wx_menu_entity`;
CREATE TABLE `wx_menu_entity` (
  `id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `type` enum('none','view','click','scancode_push','scancode_waitmsg','pic_sysphoto','pic_photo_or_album','pic_weixin','media_id','view_limited','miniprogram','location_select') NOT NULL,
  `sort` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `media_id` varchar(200) DEFAULT NULL,
  `app_id` varchar(200) DEFAULT NULL,
  `page_path` varchar(200) DEFAULT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `raw_config` varchar(2000) DEFAULT NULL,
  `plugin_code` varchar(200) DEFAULT NULL,
  `event_key` varchar(200) DEFAULT NULL,
  `display_config` text,
  `identity_type` enum('none','openId','userInfo') DEFAULT 'none',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_office`
-- ----------------------------
DROP TABLE IF EXISTS `wx_office`;
CREATE TABLE `wx_office` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `name` varchar(200) COLLATE utf8_bin NOT NULL,
  `province` varchar(200) COLLATE utf8_bin NOT NULL,
  `district` varchar(200) COLLATE utf8_bin NOT NULL,
  `detail_addr` varchar(200) COLLATE utf8_bin NOT NULL,
  `dept_id` bigint(20) NOT NULL,
  `phone` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `info` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `service_range` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `longitude` varchar(50) COLLATE utf8_bin NOT NULL,
  `latitude` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `wx_reply`
-- ----------------------------
DROP TABLE IF EXISTS `wx_reply`;
CREATE TABLE `wx_reply` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `event` enum('subscribe','unsubscribe','keyword','common','text','image','video','voice','shortvideo','location') NOT NULL,
  `tag` int(11) NOT NULL DEFAULT '0',
  `gender` enum('unknown','male','female') NOT NULL DEFAULT 'unknown',
  `name` varchar(200) NOT NULL,
  `keyword` varchar(2000) DEFAULT NULL COMMENT '使用逗号分割的关键字',
  `message_type` enum('image','voice','video','news','music','text','plugin') NOT NULL,
  `text` varchar(2000) DEFAULT NULL,
  `media_id` varchar(200) DEFAULT NULL,
  `media_name` varchar(200) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `music_url` varchar(2000) DEFAULT NULL,
  `hq_music_url` varchar(2000) DEFAULT NULL,
  `plugin_code` varchar(200) DEFAULT NULL,
  `plugin_config` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_sync`
-- ----------------------------
DROP TABLE IF EXISTS `wx_sync`;
CREATE TABLE `wx_sync` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `begin_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `finished` tinyint(4) NOT NULL DEFAULT '0',
  `notified` tinyint(4) NOT NULL DEFAULT '0',
  `actor` varchar(100) NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `action` enum('user','video','voice','file','image','thumb','news') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_user_binding`
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_binding`;
CREATE TABLE `wx_user_binding` (
  `tenant_id` bigint(20) NOT NULL,
  `open_id` varchar(200) NOT NULL,
  `local_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`open_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wx_users`
-- ----------------------------
DROP TABLE IF EXISTS `wx_users`;
CREATE TABLE `wx_users` (
  `id` bigint(100) NOT NULL COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `batch_index` bigint(11) NOT NULL DEFAULT '0',
  `subscribe` tinyint(100) NOT NULL DEFAULT '1' COMMENT '是否关注',
  `open_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'openId',
  `union_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `group_id` bigint(100) DEFAULT NULL,
  `nickname` varchar(200) DEFAULT NULL COMMENT '昵称',
  `sex` enum('male','female','unknown') CHARACTER SET utf8 DEFAULT 'unknown' COMMENT '性别',
  `city` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '城市',
  `province` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '省份',
  `country` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '国家',
  `language` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `avatar` varchar(400) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户头像',
  `subscribe_time` datetime DEFAULT NULL COMMENT '关注时间',
  `tagid_list` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Procedure structure for `CreateColumnChildList`
-- ----------------------------
DROP PROCEDURE IF EXISTS `CreateColumnChildList`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` PROCEDURE `CreateColumnChildList`(tenantid int, rootId INT, nDepth INT)
    SQL SECURITY INVOKER
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE b INT;
DECLARE cur1 CURSOR FOR SELECT id FROM cms_column where tenant_id = tenantid and parent_id = rootId;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
SET @@max_sp_recursion_depth = 256; 
OPEN cur1;
FETCH cur1 INTO b;
WHILE done = 0 DO
insert into tmpLst values (null, b, nDepth);
CALL CreateColumnChildList(tenantid, b, nDepth + 1);
FETCH cur1 INTO b;
END WHILE;
CLOSE cur1;
END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `CreateOrderTable`
-- ----------------------------
DROP PROCEDURE IF EXISTS `CreateOrderTable`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` PROCEDURE `CreateOrderTable`(tenantid int)
    SQL SECURITY INVOKER
BEGIN
set @sql = CONCAT('DROP TABLE IF EXISTS o_order_', tenantid);
PREPARE stmt1 FROM @sql;
EXECUTE stmt1 ;
DEALLOCATE PREPARE stmt1;
END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `DeletePropertyTreeById`
-- ----------------------------
DROP PROCEDURE IF EXISTS `DeletePropertyTreeById`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` PROCEDURE `DeletePropertyTreeById`(parentid int, containParent BOOL)
    SQL SECURITY INVOKER
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE b INT DEFAULT -1;
DECLARE cur1 CURSOR FOR SELECT id from cms_property WHERE parent_id = parentid;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
SET @@max_sp_recursion_depth = 256; 
OPEN cur1;
FETCH cur1 INTO b;
WHILE done = 0 DO
CALL DeletePropertyTreeById(b, 0);
FETCH cur1 INTO b;
END WHILE;
DELETE from cms_property where parent_id = parentid;
IF containParent THEN
DELETE FROM cms_property WHERE id = parentid;
END IF;
CLOSE cur1;
END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `GetColumnTreeByCode`
-- ----------------------------
DROP PROCEDURE IF EXISTS `GetColumnTreeByCode`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` PROCEDURE `GetColumnTreeByCode`(tenantid long, code varchar(64), containParent BOOL)
    SQL SECURITY INVOKER
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE b INT DEFAULT -1;
DECLARE cur1 CURSOR FOR SELECT c.id FROM cms_column c where c.tenant_id=tenantid and c.code = code;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
CREATE TEMPORARY TABLE IF NOT EXISTS tmpLst (sno int primary key auto_increment, id int, depth int);
DELETE FROM tmpLst;
OPEN cur1;
FETCH cur1 INTO b;
WHILE done = 0 DO
IF containParent THEN
insert into tmpLst values (null, b, 0);
END IF;
CALL CreateColumnChildList(tenantid, b, 0);
FETCH cur1 INTO b;
END WHILE;
CLOSE cur1;
select c.* from tmpLst, cms_column c where tmpLst.id = c.id order by tmpLst.sno;
END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `GetColumnTreeById`
-- ----------------------------
DROP PROCEDURE IF EXISTS `GetColumnTreeById`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` PROCEDURE `GetColumnTreeById`(tenantid long, parentid long, containParent BOOL)
    SQL SECURITY INVOKER
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE b INT DEFAULT -1;
DECLARE cur1 CURSOR FOR SELECT c.id FROM cms_column c where c.tenant_id=tenantid and c.id=parentid;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
CREATE TEMPORARY TABLE IF NOT EXISTS tmpLst (sno int primary key auto_increment, id int, depth int);
DELETE FROM tmpLst;
OPEN cur1;
FETCH cur1 INTO b;
WHILE done = 0 DO
IF containParent THEN
insert into tmpLst values (null, b, 0);
END IF;
CALL CreateColumnChildList(tenantid, b, 0);
FETCH cur1 INTO b;
END WHILE;
CLOSE cur1;
select c.* from tmpLst, cms_column c where tmpLst.id = c.id order by tmpLst.sno;
END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `UpdateScoreRank`
-- ----------------------------
DROP PROCEDURE IF EXISTS `UpdateScoreRank`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` PROCEDURE `UpdateScoreRank`(tenantId long, year int)
    SQL SECURITY INVOKER
BEGIN
declare dutyId INT default 0;
declare cur_duty CURSOR FOR SELECT id FROM s_duty where tenant_id = tenantId; 
declare CONTINUE HANDLER FOR SQLSTATE '02000' SET dutyId = NULL;
OPEN cur_duty;
FETCH cur_duty INTO dutyId;
WHILE ( dutyId is not null) DO 
update s_score_summary ss inner join (
SELECT obj.id, 
CASE
WHEN @rowtotal = obj.score THEN
	@rank
WHEN @rowtotal := obj.score THEN
    @rank := @rownum + 1
WHEN @rowtotal = 0 THEN
    @rank := @rownum + 1
END
AS rank,
@rownum := @rownum + 1 rownum
FROM ( select s.id, s.score 
	from s_score_summary s inner join s_user u on s.user_id = u.id 
	left join s_duty duty on duty.id = u.duty_id 
	where s.tenant_id = tenantId and s.year = year and duty.id = dutyId ORDER BY s.score DESC ) 
AS obj, ( SELECT @rownum := 0, @rank := 0, @rowtotal := NULL ) r) rr
on ss.id = rr.id set ss.duty_rank = rr.rank;
FETCH cur_duty INTO dutyId; 
END WHILE;
CLOSE cur_duty;
update s_score_summary ss inner join (
SELECT obj.id, 
CASE
WHEN @rowtotal = obj.score THEN
	@rank
WHEN @rowtotal := obj.score THEN
    @rank := @rownum + 1
WHEN @rowtotal = 0 THEN
    @rank := @rownum + 1
END
AS rank,
@rownum := @rownum + 1 rownum
FROM ( SELECT id, user_id, score FROM s_score_summary ORDER BY score DESC ) 
AS obj, ( SELECT @rownum := 0, @rank := 0, @rowtotal := NULL ) r) rr
on ss.id = rr.id set ss.rank = rr.rank;
END
 ;;
delimiter ;

-- ----------------------------
--  Function structure for `bj`
-- ----------------------------
DROP FUNCTION IF EXISTS `bj`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` FUNCTION `bj`(seq_name varchar(50)) RETURNS varchar(20) CHARSET utf8 COLLATE utf8_bin
    SQL SECURITY INVOKER
begin
	declare seq_rules varchar(255);
	declare seq_length int(2);
	declare seq_max bigint(20);
	declare max_id_length int(20);
	declare max_id varchar(20);
	select rules,g_sequence.`length`,`max` into seq_rules, seq_length,seq_max from g_sequence where name =  seq_name ;
	set max_id = seq_max;
	set max_id_length = character_length(max_id);
	
	while seq_length > max_id_length do
		set max_id = concat('0',max_id);
		set max_id_length = character_length(max_id);
	end while; 
	
	if seq_rules is not null and instr(seq_rules,'###max_id###') > 0   then
		set max_id = replace(seq_rules,'###max_id###',max_id);
	end if;
	return max_id;
end
 ;;
delimiter ;

-- ----------------------------
--  Function structure for `GetUserMissingExamineCount`
-- ----------------------------
DROP FUNCTION IF EXISTS `GetUserMissingExamineCount`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` FUNCTION `GetUserMissingExamineCount`(tenantid int, userid int) RETURNS int(11)
    SQL SECURITY INVOKER
BEGIN
DECLARE totalCount INT; 
select count(*) INTO totalCount from dc_examine e 
	where e.tenant_id = tenantid 
	and (
		e.dept_id in (0, (SELECT T2.id FROM (SELECT @r AS _id, (SELECT @r := parent_id FROM s_department WHERE id = _id) AS parent_id, @l := @l + 1 AS lvl FROM (SELECT @r := userid, @l := 0) vars,s_department h WHERE @r <> 0) T1 JOIN s_department T2 ON T1._id = T2.id ORDER BY T1.lvl DESC)) 
		or ISNULL(e.dept_id) 
		or e.duty_ids like CONCAT('%,', (select duty_id from s_user where id = userid), ',%')
		or ISNULL(e.duty_ids)) 
	and e.online_date <= NOW() 
	and (e.offline_date >= NOW() or ISNULL(e.offline_date)) 
	and (e.id not in (select s.examine_id from dc_examine_score s where s.user_id = userid));
RETURN totalCount;
END
 ;;
delimiter ;

-- ----------------------------
--  Function structure for `nextval`
-- ----------------------------
DROP FUNCTION IF EXISTS `nextval`;
delimiter ;;
CREATE DEFINER=`mysql.sys`@`localhost` FUNCTION `nextval`(seq_name varchar(50)) RETURNS varchar(20) CHARSET utf8 COLLATE utf8_bin
    SQL SECURITY INVOKER
begin
   update g_sequence set `max` = `max` + next  where name = seq_name;
   return bj(seq_name);
end
 ;;
delimiter ;

-- ----------------------------
--  Triggers structure for table cms_column
-- ----------------------------
DROP TRIGGER IF EXISTS `tri_column_add`;
delimiter ;;
CREATE TRIGGER `tri_column_add` BEFORE INSERT ON `cms_column` FOR EACH ROW BEGIN
DECLARE count INT;
DECLARE msg varchar(255);
IF !(ISNULL(new.code)) and new.code != '' then
SET count = (select count(*) from cms_column u where u.tenant_id = new.tenant_id and u.code = new.code);
IF (count > 0) THEN 
	SET msg = concat('栏目CODE重复：', cast(new.code as char));
	signal sqlstate '45000' set message_text = msg;
END IF;
END IF;
END
 ;;
delimiter ;
DROP TRIGGER IF EXISTS `tri_column_update`;
delimiter ;;
CREATE TRIGGER `tri_column_update` BEFORE UPDATE ON `cms_column` FOR EACH ROW BEGIN
DECLARE count INT;
DECLARE msg varchar(255);
IF !(ISNULL(new.code)) and new.code != '' then
SET count = (select count(*) from cms_column u where u.tenant_id = new.tenant_id and u.code = new.code and u.id != new.id);
IF (count > 0) THEN 
	SET msg = concat('栏目CODE重复：: ', cast(new.code as char));
	signal sqlstate '45000' set message_text = msg;
END IF;
END IF;
END
 ;;
delimiter ;

delimiter ;;
-- ----------------------------
--  Triggers structure for table cms_template
-- ----------------------------
 ;;
delimiter ;
DROP TRIGGER IF EXISTS `tri_template_add`;
delimiter ;;
CREATE TRIGGER `tri_template_add` BEFORE INSERT ON `cms_template` FOR EACH ROW BEGIN
DECLARE count INT;
DECLARE msg varchar(255);
IF !(ISNULL(new.code)) and new.code != '' then
SET count = (select count(*) from cms_template u where u.tenant_id = new.tenant_id and u.code = new.code);
IF (count > 0) THEN 
	SET msg = concat('模板CODE重复：', cast(new.code as char));
	signal sqlstate '45000' set message_text = msg;
END IF;
END IF;
END
 ;;
delimiter ;
DROP TRIGGER IF EXISTS `tri_template_update`;
delimiter ;;
CREATE TRIGGER `tri_template_update` BEFORE UPDATE ON `cms_template` FOR EACH ROW BEGIN
DECLARE count INT;
DECLARE msg varchar(255);
IF !(ISNULL(new.code)) and new.code != '' then
SET count = (select count(*) from cms_template u where u.tenant_id = new.tenant_id and u.code = new.code and u.id != new.id);
IF (count > 0) THEN 
	SET msg = concat('模板CODE重复：: ', cast(new.code as char));
	signal sqlstate '45000' set message_text = msg;
END IF;
END IF;
END
 ;;
delimiter ;

delimiter ;;
-- ----------------------------
--  Triggers structure for table im_group_user
-- ----------------------------
 ;;
delimiter ;
DROP TRIGGER IF EXISTS `tri_group_member_count_add`;
delimiter ;;
CREATE TRIGGER `tri_group_member_count_add` AFTER INSERT ON `im_group_user` FOR EACH ROW BEGIN
UPDATE im_group SET member_count = (select count(*) from im_group_user where group_id = new.group_id) WHERE id = new.group_id;
END
 ;;
delimiter ;

delimiter ;;
-- ----------------------------
--  Triggers structure for table s_user
-- ----------------------------
 ;;
delimiter ;
DROP TRIGGER IF EXISTS `tri_user_fullname_add`;
delimiter ;;
CREATE TRIGGER `tri_user_fullname_add` BEFORE INSERT ON `s_user` FOR EACH ROW BEGIN
DECLARE count INT;
DECLARE msg varchar(255);
IF !(ISNULL(new.phone)) and new.phone != '' then
SET count = (select count(*) from s_user u where u.tenant_id = new.tenant_id and u.phone = new.phone);
IF (count > 0) THEN 
	SET msg = concat('手机号重复: ', cast(new.phone as char));
	signal sqlstate '45000' set message_text = msg;
END IF;
END IF;
IF (ISNULL(new.lastname)) THEN
	SET new.lastname = '';
END IF;
IF ISNULL(new.firstname) THEN
	SET new.firstname = '';
END IF;
SET new.fullname = concat(new.lastname, new.firstname);
END
 ;;
delimiter ;
DROP TRIGGER IF EXISTS `tri_user_fullname_update`;
delimiter ;;
CREATE TRIGGER `tri_user_fullname_update` BEFORE UPDATE ON `s_user` FOR EACH ROW BEGIN
DECLARE count INT;
DECLARE msg varchar(255);
IF !(ISNULL(new.phone)) and new.phone != '' then
SET count = (select count(*) from s_user u where u.tenant_id = new.tenant_id and u.phone = new.phone and u.id != new.id);
IF (count > 0) THEN 
	SET msg = concat('手机号重复: ', cast(new.phone as char));
	signal sqlstate '45000' set message_text = msg;
END IF;
END IF;
IF (ISNULL(new.lastname)) THEN
	SET new.lastname = '';
END IF;
IF ISNULL(new.firstname) THEN
	SET new.firstname = '';
END IF;
SET new.fullname = concat(new.lastname, new.firstname);
END
 ;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
