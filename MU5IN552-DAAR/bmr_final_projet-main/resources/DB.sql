CREATE DATABASE IF NOT EXISTS bmr;

USE bmr;

DROP TABLE IF EXISTS t_books_0;
DROP TABLE IF EXISTS t_books_1;
DROP TABLE IF EXISTS t_books_2;
DROP TABLE IF EXISTS t_books_3;
DROP TABLE IF EXISTS t_books_4;
DROP TABLE IF EXISTS t_books_5;
DROP TABLE IF EXISTS t_books_6;
DROP TABLE IF EXISTS t_books_7;
DROP TABLE IF EXISTS t_books_8;
DROP TABLE IF EXISTS t_books_9;
DROP TABLE IF EXISTS t_books_10;
DROP TABLE IF EXISTS t_books_11;
DROP TABLE IF EXISTS t_books_12;
DROP TABLE IF EXISTS t_books_13;
DROP TABLE IF EXISTS t_books_14;
DROP TABLE IF EXISTS t_books_15;

DROP TABLE IF EXISTS t_group_0;
DROP TABLE IF EXISTS t_group_1;
DROP TABLE IF EXISTS t_group_2;
DROP TABLE IF EXISTS t_group_3;
DROP TABLE IF EXISTS t_group_4;
DROP TABLE IF EXISTS t_group_5;
DROP TABLE IF EXISTS t_group_6;
DROP TABLE IF EXISTS t_group_7;
DROP TABLE IF EXISTS t_group_8;
DROP TABLE IF EXISTS t_group_9;
DROP TABLE IF EXISTS t_group_10;
DROP TABLE IF EXISTS t_group_11;
DROP TABLE IF EXISTS t_group_12;
DROP TABLE IF EXISTS t_group_13;
DROP TABLE IF EXISTS t_group_14;
DROP TABLE IF EXISTS t_group_15;

DROP TABLE IF EXISTS t_user_0;
DROP TABLE IF EXISTS t_user_1;
DROP TABLE IF EXISTS t_user_2;
DROP TABLE IF EXISTS t_user_3;
DROP TABLE IF EXISTS t_user_4;
DROP TABLE IF EXISTS t_user_5;
DROP TABLE IF EXISTS t_user_6;
DROP TABLE IF EXISTS t_user_7;
DROP TABLE IF EXISTS t_user_8;
DROP TABLE IF EXISTS t_user_9;
DROP TABLE IF EXISTS t_user_10;
DROP TABLE IF EXISTS t_user_11;
DROP TABLE IF EXISTS t_user_12;
DROP TABLE IF EXISTS t_user_13;
DROP TABLE IF EXISTS t_user_14;
DROP TABLE IF EXISTS t_user_15;

DROP TABLE IF EXISTS t_user_bookmark_0;
DROP TABLE IF EXISTS t_user_bookmark_1;
DROP TABLE IF EXISTS t_user_bookmark_2;
DROP TABLE IF EXISTS t_user_bookmark_3;
DROP TABLE IF EXISTS t_user_bookmark_4;
DROP TABLE IF EXISTS t_user_bookmark_5;
DROP TABLE IF EXISTS t_user_bookmark_6;
DROP TABLE IF EXISTS t_user_bookmark_7;
DROP TABLE IF EXISTS t_user_bookmark_8;
DROP TABLE IF EXISTS t_user_bookmark_9;
DROP TABLE IF EXISTS t_user_bookmark_10;
DROP TABLE IF EXISTS t_user_bookmark_11;
DROP TABLE IF EXISTS t_user_bookmark_12;
DROP TABLE IF EXISTS t_user_bookmark_13;
DROP TABLE IF EXISTS t_user_bookmark_14;
DROP TABLE IF EXISTS t_user_bookmark_15;

DROP TABLE IF EXISTS t_user_preference_0;
DROP TABLE IF EXISTS t_user_preference_1;
DROP TABLE IF EXISTS t_user_preference_2;
DROP TABLE IF EXISTS t_user_preference_3;
DROP TABLE IF EXISTS t_user_preference_4;
DROP TABLE IF EXISTS t_user_preference_5;
DROP TABLE IF EXISTS t_user_preference_6;
DROP TABLE IF EXISTS t_user_preference_7;
DROP TABLE IF EXISTS t_user_preference_8;
DROP TABLE IF EXISTS t_user_preference_9;
DROP TABLE IF EXISTS t_user_preference_10;
DROP TABLE IF EXISTS t_user_preference_11;
DROP TABLE IF EXISTS t_user_preference_12;
DROP TABLE IF EXISTS t_user_preference_13;
DROP TABLE IF EXISTS t_user_preference_14;
DROP TABLE IF EXISTS t_user_preference_15;



CREATE TABLE `t_user_0`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_1`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_2`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_3`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_4`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_5`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_6`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_7`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_8`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_9`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_10`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_11`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_12`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_13`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_14`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_user_15`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`      varchar(256) DEFAULT NULL COMMENT 'username',
    `password`      varchar(512) DEFAULT NULL COMMENT 'password',
    `phone`         varchar(128) DEFAULT NULL COMMENT 'phone number',
    `mail`          varchar(512) DEFAULT NULL COMMENT 'email',
    `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
    `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_username` (`username`) USING BTREE
);

CREATE TABLE `t_group_0`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_1`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_2`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_3`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_4`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_5`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_6`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_7`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_8`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_9`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_10`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_11`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_12`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_13`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_14`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);
CREATE TABLE `t_group_15`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid`         varchar(32)  DEFAULT NULL COMMENT 'grouping identifier',
    `name`        varchar(64)  DEFAULT NULL COMMENT 'group name',
    `username`    varchar(256) DEFAULT NULL COMMENT 'Create group user name',
    `sort_order`  int(3) DEFAULT NULL COMMENT 'grouped sorting',
    `create_time` datetime     DEFAULT NULL COMMENT 'creation time',
    `update_time` datetime     DEFAULT NULL COMMENT 'modified time',
    `del_flag`    tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
    PRIMARY KEY (`id`),
    KEY           `idx_username` (`username`) USING BTREE
);

ALTER TABLE `t_group_0` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_1` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_2` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_3` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_4` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_5` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_6` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_7` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_8` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_9` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_10` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_11` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_12` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_13` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_14` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';
ALTER TABLE `t_group_15` MODIFY `gid` VARCHAR(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'grouping identifier';


CREATE TABLE `t_books_0`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_1`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_2`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_3`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_4`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_5`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_6`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_7`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_8`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_9`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_10`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_11`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_12`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_13`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_14`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;
CREATE TABLE `t_books_15`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Book ID',
    `ref_id`               varchar(50) DEFAULT NULL COMMENT 'Reference ID for external systems',
    `title`                varchar(255) DEFAULT NULL COMMENT 'Book Title',
    `author`               varchar(255) DEFAULT NULL COMMENT 'Author',
    `category`             varchar(100) DEFAULT NULL COMMENT 'Book Category',
    `description`          text DEFAULT NULL COMMENT 'Book Description',
    `language`             varchar(50) DEFAULT NULL COMMENT 'Language',
    `click_count`          int(11) DEFAULT 0 COMMENT 'Number of times the book was clicked',
    `storage_path`         varchar(512) DEFAULT NULL COMMENT 'Path to the stored book file',
    `sorted_order`         int(11) DEFAULT NULL COMMENT 'Sorted Order for frontend display',
    `deletion_time`        bigint(20) DEFAULT NULL COMMENT 'Deletion Time',
    `create_time`          datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time`          datetime DEFAULT NULL COMMENT 'Modified Time',
    `del_flag`             tinyint(1) DEFAULT NULL COMMENT 'Delete Flag 0: not deleted 1: deleted',
    `es_sync_flag` TINYINT(1) DEFAULT 0 COMMENT '0: unsynchronized 1: synchronized',
    `img` VARCHAR(512) DEFAULT NULL COMMENT 'Book Image Path',    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_ref_id` (`ref_id`) USING BTREE
)  CHARACTER SET utf8mb4
   COLLATE utf8mb4_general_ci;


CREATE TABLE `t_user_preference_0` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_1` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_2` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_3` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_4` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_5` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_6` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_7` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_8` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_9` (
                                       `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                       `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                       `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                       `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                       `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                       `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                       `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                       `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                       `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_10` (
                                        `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                        `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                        `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                        `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                        `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                        `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                        `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                        `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                        `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_11` (
                                        `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                        `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                        `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                        `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                        `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                        `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                        `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                        `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                        `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_12` (
                                        `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                        `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                        `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                        `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                        `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                        `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                        `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                        `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                        `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_13` (
                                        `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                        `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                        `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                        `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                        `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                        `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                        `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                        `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                        `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_14` (
                                        `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                        `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                        `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                        `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                        `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                        `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                        `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                        `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                        `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;
CREATE TABLE `t_user_preference_15` (
                                        `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
                                        `user_name`           VARCHAR(100) NOT NULL COMMENT 'user ID',
                                        `author`            VARCHAR(255) DEFAULT NULL COMMENT 'Favorite Authors',
                                        `category`          VARCHAR(100) DEFAULT NULL COMMENT 'Favorite Categories',
                                        `like_count`        INT(11) DEFAULT 0 COMMENT 'Number of user clicks/reads',
                                        `deletion_time` bigint(20) DEFAULT NULL COMMENT 'deletion time',
                                        `create_time`   datetime     DEFAULT NULL COMMENT 'creation time',
                                        `update_time`   datetime     DEFAULT NULL COMMENT 'modified time',
                                        `del_flag`      tinyint(1) DEFAULT NULL COMMENT 'Delete flag 0: not deleted 1: deleted',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_author_category`(`user_name`,`author`,`category`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

create table t_user_bookmark_0
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_1
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_2
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_3
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_4
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_5
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_6
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_7
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_8
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_9
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_10
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_11
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_12
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_13
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_14
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
create table t_user_bookmark_15
(
    id          bigint auto_increment primary key,
    gid         varchar(20) not null,
    username    varchar(50) not null,
    bookId      bigint      not null,
    create_time datetime    null comment 'creation time',
    update_time datetime    null comment 'modified time',
    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'
);
