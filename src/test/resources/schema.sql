-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `email`        TEXT                                  NOT NULL,
    `is_activated` BOOLEAN     DEFAULT FALSE             NOT NULL,
    `password`     CHAR(60)                              NOT NULL,
    `role`         VARCHAR(16) DEFAULT 'USER'            NOT NULL,
    `username`     VARCHAR(16)                           NOT NULL,
    `is_deleted`   BOOLEAN     DEFAULT FALSE             NOT NULL,
    `create_time`  DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `delete_time`  DATETIME
);

-- 分类表
CREATE TABLE IF NOT EXISTS `category`
(
    `id`        INT AUTO_INCREMENT PRIMARY KEY,
    `parent_id` INT,
    `order`     INT DEFAULT 0 NOT NULL,
    `name`      VARCHAR(16)   NOT NULL
);

-- 标签表
CREATE TABLE IF NOT EXISTS `tag`
(
    `id`   INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(16) NOT NULL UNIQUE
);

-- 图书表
CREATE TABLE IF NOT EXISTS `book`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `isbn`         CHAR(13)      NOT NULL UNIQUE,
    `title`        VARCHAR(128)  NOT NULL,
    `subtitle`     VARCHAR(128),
    `author`       VARCHAR(128)  NOT NULL,
    `publisher`    VARCHAR(128)  NOT NULL,
    `publish_date` DATE          NOT NULL,
    `edition`      VARCHAR(32)   NOT NULL,
    `page_count`   INT           NOT NULL,
    `language`     VARCHAR(32)   NOT NULL,
    `description`  TEXT          NOT NULL,
    `price`        DECIMAL(8, 2) NOT NULL
);

-- 图书副本表
CREATE TABLE IF NOT EXISTS `book_copy`
(
    `id`             INT AUTO_INCREMENT PRIMARY KEY,
    `book_id`        INT                                   NOT NULL,
    `purchase_price` DECIMAL(8, 2)                         NOT NULL,
    `purchase_date`  DATE                                  NOT NULL,
    `source`         VARCHAR(64)                           NOT NULL,
    `status`         VARCHAR(16) DEFAULT 'AVAILABLE'       NOT NULL,
    `entry_by`       INT                                   NOT NULL,
    `create_time`    DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `remove_time`    DATETIME
);

-- 书评表
CREATE TABLE IF NOT EXISTS `book_review`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     INT                                NOT NULL,
    `book_id`     INT                                NOT NULL,
    `score`       INT                                NOT NULL,
    `content`     TEXT                               NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 收藏表
CREATE TABLE IF NOT EXISTS `user_book_like`
(
    `user_id`     INT                                NOT NULL,
    `book_id`     INT                                NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`user_id`, `book_id`)
);

-- 书评点赞表
CREATE TABLE IF NOT EXISTS `user_book_review_like`
(
    `user_id`        INT                                NOT NULL,
    `book_review_id` INT                                NOT NULL,
    `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`user_id`, `book_review_id`)
);

-- 借阅记录表
CREATE TABLE IF NOT EXISTS `borrow_record`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`      INT                                   NOT NULL,
    `book_copy_id` INT                                   NOT NULL,
    `is_renewed`   BOOLEAN     DEFAULT FALSE             NOT NULL,
    `close_status` VARCHAR(16) DEFAULT 'OPEN'            NOT NULL,
    `create_time`  DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `due_time`     DATETIME                              NOT NULL,
    `close_time`   DATETIME
);

-- 图书与标签关系表
CREATE TABLE IF NOT EXISTS `book_tag_relation`
(
    `book_id` INT NOT NULL,
    `tag_id`  INT NOT NULL,
    PRIMARY KEY (`book_id`, `tag_id`)
);

-- 激活令牌表
CREATE TABLE IF NOT EXISTS `activation_token`
(
    `user_id`     INT PRIMARY KEY,
    `token`       CHAR(32) NOT NULL,
    `expire_time` DATETIME NOT NULL
);

-- 邮箱验证码表
CREATE TABLE IF NOT EXISTS `email_verification`
(
    `user_id`       INT                                NOT NULL,
    `business_type` VARCHAR(16)                        NOT NULL,
    `code`          CHAR(9)                            NOT NULL,
    `is_used`       BOOLEAN  DEFAULT FALSE             NOT NULL,
    `create_time`   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `send_time`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`user_id`, `business_type`)
);

-- 人机验证码表
CREATE TABLE IF NOT EXISTS `captcha`
(
    `token`       CHAR(32) PRIMARY KEY,
    `captcha`     CHAR(4)  NOT NULL,
    `expire_time` DATETIME NOT NULL
);