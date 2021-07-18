create schema `system`;
use
`system`;


SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user`
VALUES (NULL, 'admin', '123', '主租户管理员');
COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;



create schema `order`;
use
`order`;


SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`
(
    `id`         int         NOT NULL AUTO_INCREMENT,
    `uid`        int         NOT NULL,
    `order_name` varchar(32) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

-- ----------------------------
-- Records of order
-- ----------------------------
BEGIN;
INSERT INTO `order`
VALUES (1, 1, '租户2订单');
COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;