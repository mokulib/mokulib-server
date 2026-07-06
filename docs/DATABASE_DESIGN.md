# MokuLib Database Design

## user (用户)

| Name         | DataType    | Default           | AutoInc | PK/UK/IDX | NotNull |
|:-------------|:------------|:------------------|:--------|:----------|:--------|
| id           | int         |                   | Y       | PK        | Y       |
| email        | text        |                   |         |           | Y       |
| is_activated | boolean     | false             |         |           | Y       |
| password     | char(60)    |                   |         |           | Y       |
| role         | varchar(16) | 'USER'            |         |           | Y       |
| username     | varchar(16) |                   |         |           | Y       |
| is_deleted   | boolean     | false             |         |           | Y       |
| create_time  | datetime    | current_timestamp |         |           | Y       |
| delete_time  | datetime    |                   |         |           |         |

role: 'ADMIN', 'USER'

## category (分类)

| Name      | DataType    | Default | AutoInc | PK/UK/IDX | NotNull |
|:----------|:------------|:--------|:--------|:----------|:--------|
| id        | int         |         | Y       | PK        | Y       |
| parent_id | int         |         |         |           |         |
| order     | int         | 0       |         |           | Y       |
| name      | varchar(16) |         |         |           | Y       |

## tag (标签)

| Name      | DataType    | Default | AutoInc | PK/UK/IDX | NotNull |
|:----------|:------------|:--------|:--------|:----------|:--------|
| id        | int         |         | Y       | PK        | Y       |
| name      | varchar(16) |         |         | UK        | Y       |

## book (图书)

| Name         | DataType     | Default | AutoInc | PK/UK/IDX | NotNull |
|:-------------|:-------------|:--------|:--------|:----------|:--------|
| id           | int          |         | Y       | PK        | Y       |
| isbn         | char(13)     |         |         | UK        | Y       |
| title        | varchar(128) |         |         |           | Y       |
| subtitle     | varchar(128) |         |         |           |         |
| author       | varchar(128) |         |         |           | Y       |
| publisher    | varchar(128) |         |         |           | Y       |
| publish_date | date         |         |         |           | Y       |
| edition      | varchar(32)  |         |         |           | Y       |
| page_count   | int          |         |         |           | Y       |
| language     | varchar(32)  |         |         |           | Y       |
| description  | text         |         |         |           | Y       |
| price        | decimal(8,2) |         |         |           | Y       |

## book_copy (图书副本)

| Name           | DataType     | Default           | AutoInc | PK/UK/IDX | NotNull |
|:---------------|:-------------|:------------------|:--------|:----------|:--------|
| id             | int          |                   | Y       | PK        | Y       |
| book_id        | int          |                   |         |           | Y       |
| purchase_price | decimal(8,2) |                   |         |           | Y       |
| purchase_date  | date         |                   |         |           | Y       |
| source         | varchar(64)  |                   |         |           | Y       |
| status         | varchar(16)  | 'AVAILABLE'       |         |           | Y       |
| entry_by       | int          |                   |         |           | Y       |
| create_time    | datetime     | current_timestamp |         |           | Y       |
| remove_time    | datetime     |                   |         |           |         |

status: 'AVAILABLE', 'UNAVAILABLE', 'WITHDRAWN'

## book_review (书评)

| Name         | DataType | Default           | AutoInc | PK/UK/IDX | NotNull |
|:-------------|:---------|:------------------|:--------|:----------|:--------|
| id           | int      |                   | Y       | PK        | Y       |
| user_id      | int      |                   |         |           | Y       |
| book_id      | int      |                   |         |           | Y       |
| score        | int      |                   |         |           | Y       |
| content      | text     |                   |         |           | Y       |
| create_time  | datetime | current_timestamp |         |           | Y       |

## user_book_like (收藏)

| Name        | DataType | Default           | AutoInc | PK/UK/IDX | NotNull |
|:------------|:---------|:------------------|:--------|:----------|:--------|
| user_id     | int      |                   |         | PK(Union) | Y       |
| book_id     | int      |                   |         | PK(Union) | Y       |
| create_time | datetime | current_timestamp |         |           | Y       |

## user_book_review_like (书评点赞)

| Name           | DataType | Default           | AutoInc | PK/UK/IDX | NotNull |
|:---------------|:---------|:------------------|:--------|:----------|:--------|
| user_id        | int      |                   |         | PK(Union) | Y       |
| book_review_id | int      |                   |         | PK(Union) | Y       |
| create_time    | datetime | current_timestamp |         |           | Y       |

## borrow_record (借阅记录)

| Name         | DataType    | Default           | AutoInc | PK/UK/IDX | NotNull |
|:-------------|:------------|:------------------|:--------|:----------|:--------|
| id           | int         |                   | Y       | PK        | Y       |
| user_id      | int         |                   |         |           | Y       |
| book_copy_id | int         |                   |         |           | Y       |
| is_renewed   | boolean     | false             |         |           | Y       |
| close_status | varchar(16) | 'OPEN'            |         |           | Y       |
| create_time  | datetime    | current_timestamp |         |           | Y       |
| due_time     | datetime    |                   |         |           | Y       |
| close_time   | datetime    |                   |         |           |         |

close_status: 'OPEN', 'CLOSED', 'LOST', 'DAMAGED'

## book_tag_relation (图书与标签关系)

| Name    | DataType | Default | AutoInc | PK/UK/IDX | NotNull |
|:--------|:---------|---------|---------|:----------|:--------|
| book_id | int      |         |         | PK(Union) | Y       |
| tag_id  | int      |         |         | PK(Union) | Y       |

## activation_token (激活令牌)

| Name        | DataType | Default | AutoInc | PK/UK/IDX | NotNull |
|:------------|:---------|:--------|:--------|:----------|:--------|
| user_id     | int      |         |         | PK        | Y       |
| token       | char(32) |         |         |           | Y       |
| expire_time | datetime |         |         |           | Y       |

## email_captcha (邮箱验证码)

| Name          | DataType    | Default | AutoInc | PK/UK/IDX | NotNull |
|:--------------|:------------|:--------|:--------|:----------|:--------|
| user_id       | int         |         |         | PK(Union) | Y       |
| business_type | varchar(16) |         |         | PK(Union) | Y       |
| captcha       | char(9)     |         |         |           | Y       |
| is_used       | boolean     | false   |         |           | Y       |
| cooling_time  | datetime    |         |         |           | Y       |
| expire_time   | datetime    |         |         |           | Y       |

business_type: 'LOGIN', 'CLOSE_ACCOUNT'

## image_captcha (图片验证码)

| Name        | DataType | Default | AutoInc | PK/UK/IDX | NotNull |
|:------------|:---------|:--------|:--------|:----------|:--------|
| token       | char(32) |         |         | PK        | Y       |
| captcha     | char(4)  |         |         |           | Y       | 
| expire_time | datetime |         |         |           | Y       |

token: 默认值由 MybatisPlus 的 IdType.ASSIGN_UUID 生成