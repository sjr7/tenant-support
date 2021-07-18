create schema `system`;

use `system`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_data_source
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_source`;
CREATE TABLE `sys_data_source`
(
    `datasource_id`     int         NOT NULL AUTO_INCREMENT,
    `driver_class_name` varchar(32) NOT NULL,
    `url`               varchar(64) NOT NULL,
    `username`          varchar(20) NOT NULL,
    `password`          varchar(64) NOT NULL,
    `db_type`           varchar(16) NOT NULL,
    `create_time`       timestamp   NOT NULL,
    `update_time`       timestamp   NULL DEFAULT NULL,
    `tenant_id`         varchar(32) NOT NULL,
    `datasource_name`   varchar(20) NOT NULL,
    PRIMARY KEY (`datasource_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='数据源表';

-- ----------------------------
-- Records of sys_data_source
-- ----------------------------
BEGIN;
INSERT INTO `sys_data_source`
VALUES (1, 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3307/system', 'root', '123456', 'mysql',
        '2021-07-18 15:50:30', NULL, '1001', 'system');
INSERT INTO `sys_data_source`
VALUES (2, 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3307/order', 'root', '123456', 'mysql',
        '2021-07-18 15:50:30', NULL, '1001', 'order');
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`
(
    `tenant_id`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `tenant_name`   varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `tenant_code`   varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `system_tenant` tinyint(1)                                                   NOT NULL,
    `create_time`   timestamp                                                    NOT NULL,
    `update_time`   timestamp                                                    NULL DEFAULT NULL,
    `active`        tinyint(1)                                                   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='租户信息表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant`
VALUES ('1000', '主租户', 'tenant_master', 1, '2021-07-18 14:43:26', NULL, 1);
INSERT INTO `sys_tenant`
VALUES ('1001', '租户2', 'tenant_2', 0, '2021-07-18 14:48:18', NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`       int         DEFAULT NULL,
    `username` varchar(32) DEFAULT NULL,
    `password` varchar(32) DEFAULT NULL,
    `alias`    varchar(32) NOT NULL,
    UNIQUE KEY `sys_user_alias_uindex` (`alias`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='测试用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user`
VALUES (NULL, 'admin', '123', '主租户管理员');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;




create schema `order`;
use `order`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `uid` int NOT NULL COMMENT 'uid ',
                         `order_name` varchar(32) NOT NULL COMMENT 'order name',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

-- ----------------------------
-- Records of order
-- ----------------------------
BEGIN;
INSERT INTO `order` VALUES (1, 1, '租户1订单');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;