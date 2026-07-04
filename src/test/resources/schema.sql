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

CREATE TABLE IF NOT EXISTS `category`
(
    `id`        INT AUTO_INCREMENT PRIMARY KEY,
    `parent_id` INT,
    `order`     INT DEFAULT 0 NOT NULL,
    `name`      VARCHAR(16)   NOT NULL
);

CREATE TABLE IF NOT EXISTS `tag`
(
    `id`   INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(16) NOT NULL UNIQUE
);

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

CREATE TABLE IF NOT EXISTS `book_review`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     INT                                NOT NULL,
    `book_id`     INT                                NOT NULL,
    `score`       INT                                NOT NULL,
    `content`     TEXT                               NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_book_like`
(
    `user_id`     INT                                NOT NULL,
    `book_id`     INT                                NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`user_id`, `book_id`)
);

CREATE TABLE IF NOT EXISTS `user_book_review_like`
(
    `user_id`        INT                                NOT NULL,
    `book_review_id` INT                                NOT NULL,
    `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`user_id`, `book_review_id`)
);

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

CREATE TABLE IF NOT EXISTS `book_tag_relation`
(
    `book_id` INT NOT NULL,
    `tag_id`  INT NOT NULL,
    PRIMARY KEY (`book_id`, `tag_id`)
);

CREATE TABLE IF NOT EXISTS `activation_token`
(
    `user_id`     INT PRIMARY KEY,
    `token`       CHAR(32) NOT NULL,
    `expire_time` DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS `email_captcha`
(
    `user_id`       INT                                NOT NULL,
    `business_type` VARCHAR(16)                        NOT NULL,
    `captcha`       CHAR(9)                            NOT NULL,
    `is_used`       BOOLEAN  DEFAULT FALSE             NOT NULL,
    `cooling_time`  DATETIME                           NOT NULL,
    `expire_time`   DATETIME                           NOT NULL,
    PRIMARY KEY (`user_id`, `business_type`)
);

CREATE TABLE IF NOT EXISTS `captcha`
(
    `token`       CHAR(32) PRIMARY KEY,
    `captcha`     CHAR(4)  NOT NULL,
    `expire_time` DATETIME NOT NULL
);