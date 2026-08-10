# MokuLib API Design

| URL                              | Function | Note                          |
|----------------------------------|----------|-------------------------------|
| /api/auth/activate/{token}       | POST     | 激活账户                      |
| /api/auth/change-password        | GET      | 请求邮箱验证码·修改密码       |
| /api/auth/change-password +      | POST     | 修改密码                      |
| /api/auth/close-account          | GET      | 关闭账户 · 请求邮箱验证码     |
| /api/auth/close-account +        | POST     | 关闭账户                      |
| /api/auth/login *                | GET      | 登录 · 请求邮箱验证码         |
| /api/auth/login *                | POST     | 邮箱密码登录 or 注册          |
| /api/auth/login +                | POST     | 邮箱验证码登录                |
| /api/auth/ping                   | GET      | token 校验                    |
| /api/captcha                     | GET      | 请求图片验证码                |
| /api/books                       | POST     | 新建书籍信息                  |
| /api/books/{id}                  | GET      | 获取书籍信息                  |
| /api/books/{id}                  | PUT      | 修改书籍信息                  |
| /api/books/{id}                  | DELETE   | 删除书籍信息                  |
| /api/books/{id}/book-copies      | GET      | 获取书籍的馆藏信息            |
| /api/books/{id}/cover            | POST     | 上传书籍封面                  |
| /api/books/{bookId}/tags         | GET      | 获取书籍的全部标签            |
| /api/books/{bookId}/tags         | POST     | 为书籍添加标签                |
| /api/books/{bookId}/tags/{tagId} | DELETE   | 删除书籍的标签                |
| /api/tags                        | GET      | 获取全部标签                  |
| /api/tags                        | POST     | 新建标签                      |
| /api/favorites/{bookId}          | GET      | 用户是否收藏书籍              |
| /api/favorites/{bookId}          | POST     | 收藏书籍                      |
| /api/favorites/{bookId}          | DELETE   | 取消收藏                      |
| /api/categories                  | GET      | 获取全部分类                  |
| /api/categories                  | POST     | 新建分类                      |
| /api/categories/{id}             | GET      | 获取分类信息                  |
| /api/book-copies                 | POST     | 新建馆藏信息                  |
| /api/book-copies/{id}            | PUT      | 修改入库信息                  |
| /api/book-copies/{id}/borrow     | POST     | 借阅馆藏                      |
| /api/book-copies/{id}/relist     | POST     | 重新上架馆藏                  |
| /api/book-copies/{id}/withdrawn  | POST     | 下架馆藏                      |
| /api/users                       | GET      | 使用 id 或 email 查询用户信息 |
| /api/users/usernames             | GET      | 使用 ids 批量查询用户名       |
| /api/users/{id}/avatar           | POST     | 上传用户头像                  |
| /api/borrow-records/{id}/renew   | POST     | 续借                          |
| /api/borrow-records/{id}/return  | POST     | 归还                          |

备注：
- 加 `*` 的接口，需要在请求时附带图片验证码相关参数。
- 加 `+` 的接口，需要在请求时附带邮箱验证码相关参数。