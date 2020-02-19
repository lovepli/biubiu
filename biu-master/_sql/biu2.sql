/*
 Navicat Premium Data Transfer

 Source Server         : docker_mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3307
 Source Schema         : biu

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 19/02/2020 16:36:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm` (
  `pval` varchar(50) NOT NULL COMMENT '权限值，shiro的权限控制表达式',
  `parent` varchar(25) DEFAULT NULL COMMENT '父权限id',
  `pname` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `ptype` int(3) DEFAULT NULL COMMENT '权限类型：1.菜单 2.按钮 3.接口 4.特殊',
  `leaf` tinyint(1) DEFAULT NULL COMMENT '是否叶子节点',
  `created` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`pval`),
  UNIQUE KEY `pval` (`pval`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限';

-- ----------------------------
-- Records of sys_perm
-- ----------------------------
BEGIN;
INSERT INTO `sys_perm` VALUES ('*', NULL, '所有权限', 0, NULL, '2018-04-19 18:14:12', NULL);
INSERT INTO `sys_perm` VALUES ('a:auth', NULL, '登录模块', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:gradleBuild', 'a:test', '构建gradle', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:mvn:install', 'a:test', 'mvnInstall', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:option', NULL, '选项模块', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:add', 'a:sys:perm', '添加权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:all:query', 'a:sys:perm', '查询出所有的权限信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:api:query', 'a:sys:perm', '查询出接口权限信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:button:query', 'a:sys:perm', '查询出按钮权限信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:delete', 'a:sys:perm', '删除权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:menu:query', 'a:sys:perm', '查询出菜单权限信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:meta:api', 'a:sys:perm', '查询接口权限元数据信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:perm:update', 'a:sys:perm', '修改权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:add', 'a:sys:role', '添加角色', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:delete', 'a:sys:role', '删除角色', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:perm:add', 'a:sys:role', '添加角色权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:perm:delete', 'a:sys:role', '删除角色权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:perm:find', 'a:sys:role', '查找角色权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:perm:update', 'a:sys:role', '修改角色权限', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:query', 'a:sys:role', '查询出所有角色', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:role:update', 'a:sys:role', '编辑角色信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:perm', NULL, '系统权限模块', 3, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:role', NULL, '系统角色模块', 3, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user', NULL, '系统用户模块', 3, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:add', 'a:sys:user', '添加系统用户', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:del', 'a:sys:user', '删除系统用户', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:info', 'a:sys:user', '查询系统用户信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:info:update', 'a:sys:user', '更新系统用户的信息', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:list', 'a:sys:user', '查询所有系统用户', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:role:find', 'a:sys:user', '查找系统用户的角色', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:sys:user:role:update', 'a:sys:user', '更新系统用户的角色', 3, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('a:test', NULL, '测试模块模块', 3, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('b:perm:add', 'm:sys:perm', '添加按钮权限', 2, NULL, '2020-02-19 01:30:51', NULL);
INSERT INTO `sys_perm` VALUES ('b:perm:api', 'm:sys:perm', '同步接口权限', 2, NULL, '2020-02-19 01:38:07', '2020-02-19 01:38:43');
INSERT INTO `sys_perm` VALUES ('b:perm:delete', 'm:sys:perm', '删除按钮权限', 2, NULL, '2020-02-19 01:31:49', NULL);
INSERT INTO `sys_perm` VALUES ('b:perm:like', 'm:sys:perm', '权限模糊查询', 2, NULL, '2020-02-19 01:47:26', '2020-02-19 15:46:18');
INSERT INTO `sys_perm` VALUES ('b:perm:menu', 'm:sys:perm', '同步菜单权限', 2, NULL, '2020-02-19 01:37:25', '2020-02-19 01:38:53');
INSERT INTO `sys_perm` VALUES ('b:perm:update', 'm:sys:perm', '编辑按钮权限', 2, NULL, '2020-02-19 01:31:25', NULL);
INSERT INTO `sys_perm` VALUES ('b:role:add', 'm:sys:role', '新增角色', 2, NULL, '2020-02-19 01:10:19', NULL);
INSERT INTO `sys_perm` VALUES ('b:role:delete', 'm:sys:role', '删除角色', 2, NULL, '2020-02-19 01:10:55', NULL);
INSERT INTO `sys_perm` VALUES ('b:role:perm:update', 'm:sys:role', '修改角色的权限', 2, NULL, '2020-02-19 01:12:50', NULL);
INSERT INTO `sys_perm` VALUES ('b:role:query', 'm:sys:role', '查询角色', 2, NULL, '2020-02-19 01:11:44', NULL);
INSERT INTO `sys_perm` VALUES ('b:role:update', 'm:sys:role', '编辑角色', 2, NULL, '2020-02-19 01:11:22', NULL);
INSERT INTO `sys_perm` VALUES ('b:user:add', 'm:sys:user', '添加用户', 2, 1, '2018-06-02 11:00:37', '2020-02-19 00:53:37');
INSERT INTO `sys_perm` VALUES ('b:user:delete', 'm:sys:user', '删除用户', 2, 1, '2018-06-02 11:00:56', '2020-02-19 00:53:31');
INSERT INTO `sys_perm` VALUES ('b:user:query', 'm:sys:user', '查询用户', 2, NULL, '2020-02-19 00:53:01', NULL);
INSERT INTO `sys_perm` VALUES ('b:user:role:update', 'm:sys:user', '修改用户的角色', 2, NULL, '2020-02-19 01:00:38', NULL);
INSERT INTO `sys_perm` VALUES ('b:user:update', 'm:sys:user', '编辑用户', 2, NULL, '2020-02-19 00:46:23', '2020-02-19 00:57:03');
INSERT INTO `sys_perm` VALUES ('m:menu1', NULL, '菜单1', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu2', NULL, '菜单2', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu3', NULL, '菜单3', 1, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu3:1', 'm:menu3', '菜单3-1', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu3:2', 'm:menu3', '菜单3-2', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu3:3', 'm:menu3', '菜单3-3', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu4', NULL, '菜单4', 1, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu4:1', 'm:menu4', '菜单4-1', 1, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu4:1:a', 'm:menu4:1', '菜单4-1-a', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu4:1:b', 'm:menu4:1', '菜单4-1-b', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu4:1:c', 'm:menu4:1', '菜单4-1-c', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:menu4:2', 'm:menu4', '菜单4-2', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:sys', NULL, '系统', 1, 0, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:sys:perm', 'm:sys', '权限管理', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:sys:role', 'm:sys', '角色管理', 1, 1, NULL, NULL);
INSERT INTO `sys_perm` VALUES ('m:sys:user', 'm:sys', '用户管理', 1, 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `rid` varchar(25) NOT NULL COMMENT '角色id',
  `rname` varchar(50) DEFAULT NULL COMMENT '角色名，用于显示',
  `rdesc` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `rval` varchar(100) NOT NULL COMMENT '角色值，用于权限判断',
  `created` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`rid`),
  UNIQUE KEY `rval` (`rval`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1002748319131680769', '普通用户', '具有一般的权限，不具备系统菜单权限1', 'common', '2018-06-02 11:06:44', '2018-06-02 11:10:57');
INSERT INTO `sys_role` VALUES ('1002806178141937666', '财务', '拥有财务相关权限', 'finance', '2018-06-02 14:56:39', NULL);
INSERT INTO `sys_role` VALUES ('1002806220860923906', '仓管', '拥有财务相关权限', 'stock', '2018-06-02 14:56:49', NULL);
INSERT INTO `sys_role` VALUES ('1002806266750803970', '销售', '拥有财务相关权限', 'sale', '2018-06-02 14:57:00', NULL);
INSERT INTO `sys_role` VALUES ('1002807171923550210', '文员', '拥有文员相关的权限', 'stuff', '2018-06-02 15:00:36', NULL);
INSERT INTO `sys_role` VALUES ('1227874893463502850', 'Java开发工程师', '负责后端项目开发', 'java', '2020-02-13 16:39:22', NULL);
INSERT INTO `sys_role` VALUES ('999999888888777777', '超级管理员', '具有本系统中最高权限', 'root', '2018-04-19 17:34:33', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_perm`;
CREATE TABLE `sys_role_perm` (
  `role_id` varchar(25) NOT NULL,
  `perm_val` varchar(25) NOT NULL,
  `perm_type` int(5) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`perm_val`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_perm
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:perm:add', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:perm:all:query', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:perm:api:query', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:perm:button:query', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:perm:delete', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:perm:menu:query', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:role:add', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:role:perm:add', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:role:perm:delete', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:role:perm:find', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:role:perm:update', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:role:query', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:perm', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:role', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:add', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:del', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:info', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:info:update', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:list', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:role:find', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'a:sys:user:role:update', 3);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:perm:add', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:perm:delete', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:role:add', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:role:delete', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:role:perm:update', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:role:query', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:role:update', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:user:add', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:user:delete', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:user:query', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:user:role:update', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'b:user:update', 2);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'm:sys', 1);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'm:sys:perm', 1);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'm:sys:role', 1);
INSERT INTO `sys_role_perm` VALUES ('1002748319131680769', 'm:sys:user', 1);
INSERT INTO `sys_role_perm` VALUES ('1227874893463502850', 'm:menu1', 1);
INSERT INTO `sys_role_perm` VALUES ('1227874893463502850', 'm:menu2', 1);
INSERT INTO `sys_role_perm` VALUES ('1227874893463502850', 'm:sys', 1);
INSERT INTO `sys_role_perm` VALUES ('1227874893463502850', 'm:sys:user', 1);
INSERT INTO `sys_role_perm` VALUES ('999999888888777777', '*', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `uid` varchar(25) NOT NULL COMMENT '用户id',
  `uname` varchar(50) DEFAULT NULL COMMENT '登录名，不可改',
  `nick` varchar(50) DEFAULT NULL COMMENT '用户昵称，可改',
  `pwd` varchar(200) DEFAULT NULL COMMENT '已加密的登录密码',
  `salt` varchar(200) DEFAULT NULL COMMENT '加密盐值',
  `lock` tinyint(1) DEFAULT NULL COMMENT '是否锁定',
  `created` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1002748017179541505', 'guanyu', '关羽', 'n2Wd7JramFVrHcijY4KW1rNTGKnwyYPJ0RDYvy2BdK0=', 'aem4EsAFae5rObEdZP4Xlw==', NULL, '2018-06-02 11:05:32', '2018-06-02 14:40:01');
INSERT INTO `sys_user` VALUES ('1002748102537822209', 'zhangfei', '张飞', 'g+aRBmgVTTPkNLNwJfM64D8rwH94WEgDgckQ4fuQp6w=', 'Sqhvxsnc0HZSQEFKjBB9zQ==', NULL, '2018-06-02 11:05:52', NULL);
INSERT INTO `sys_user` VALUES ('1227875203292545025', 'lipan', '李攀', 'QJTzqL/jeutUO1T70cVR4xWsaSGlFQJUvthMSJROUYI=', '+/dMeBUjVygEqU1LaL/iKw==', NULL, '2020-02-13 16:40:36', '2020-02-18 23:06:15');
INSERT INTO `sys_user` VALUES ('986177923098808322', 'admin', '刘备', 'J/ms7qTJtqmysekuY8/v1TAS+VKqXdH5sB7ulXZOWho=', 'wxKYXuTPST5SG0jMQzVPsg==', 0, '2018-04-17 17:41:53', '2018-04-19 17:08:15');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(25) NOT NULL,
  `role_id` varchar(25) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('1002748017179541505', '1002748319131680769');
INSERT INTO `sys_user_role` VALUES ('1002748102537822209', '1002748319131680769');
INSERT INTO `sys_user_role` VALUES ('1227875203292545025', '1227874893463502850');
INSERT INTO `sys_user_role` VALUES ('986177923098808322', '999999888888777777');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
