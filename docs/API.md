# MokuLib API Design

| URL                                      | Function | Note                          |
|------------------------------------------|----------|-------------------------------|
| /api/auth/activate/{token}               | POST     | 激活账户                      |
| /api/auth/close-account                  | GET      | 关闭账户 · 请求邮箱验证码     |
| /api/auth/close-account +                | POST     | 关闭账户                      |
| /api/auth/login *                        | GET      | 登录 · 请求邮箱验证码         |
| /api/auth/login *                        | POST     | 邮箱密码登录 or 注册          |
| /api/auth/login +                        | POST     | 邮箱验证码登录                |
| /api/auth/ping                           | GET      | token 校验                    |
| /api/auth/reset-password                 | GET      | 请求邮箱验证码·修改密码       |
| /api/auth/reset-password +               | POST     | 修改密码                      |
| /api/captcha                             | GET      | 请求图片验证码                |
| /api/books                               | POST     | 新建书籍信息                  |
| /api/books/search                        | GET      | 搜索                          |
| /api/books/{id}                          | GET      | 获取书籍信息                  |
| /api/books/{id}                          | PUT      | 修改书籍信息                  |
| /api/books/{id}                          | DELETE   | 删除书籍信息                  |
| /api/books/{id}/book-copies              | GET      | 获取书籍的馆藏信息            |
| /api/books/{id}/cover                    | POST     | 上传书籍封面                  |
| /api/books/{bookId}/tags                 | GET      | 获取书籍的全部标签            |
| /api/books/{bookId}/tags                 | POST     | 为书籍添加标签                |
| /api/books/{bookId}/tags/{tagId}         | DELETE   | 删除书籍的标签                |
| /api/tags                                | GET      | 获取全部标签                  |
| /api/tags                                | POST     | 新建标签                      |
| /api/favorites/{bookId}                  | GET      | 用户是否收藏书籍              |
| /api/favorites/{bookId}                  | POST     | 收藏书籍                      |
| /api/favorites/{bookId}                  | DELETE   | 取消收藏                      |
| /api/categories                          | GET      | 获取全部分类                  |
| /api/categories                          | POST     | 新建分类                      |
| /api/categories/{id}                     | GET      | 获取分类信息                  |
| /api/categories/{id}/books/page          | GET      | 按分类获取图书信息            |
| /api/book-copies                         | POST     | 新建馆藏信息                  |
| /api/book-copies/{id}                    | GET      | 获取馆藏信息                  |
| /api/book-copies/{id}                    | PUT      | 修改入库信息                  |
| /api/book-copies/{id}/borrow             | POST     | 借阅馆藏                      |
| /api/book-copies/{id}/borrow-records     | GET      | 获取馆藏的全部借阅记录        |
| /api/book-copies/{id}/relist             | POST     | 重新上架馆藏                  |
| /api/book-copies/{id}/withdrawn          | POST     | 下架馆藏                      |
| /api/users                               | GET      | 使用 id 或 email 查询用户信息 |
| /api/users/borrowing                     | GET      | 查询用户的借阅中记录          |
| /api/users/favorites                     | GET      | 查询用户的收藏记录            |
| /api/users/history                       | GET      | 查询用户的借阅历史记录        |
| /api/users/list                          | GET      | 批量查询用户列表              |
| /api/users/username                      | POST     | 修改用户名                    |
| /api/users/{id}/avatar                   | POST     | 上传用户头像                  |
| /api/borrow-records/{id}/renew           | POST     | 续借                          |
| /api/borrow-records/{id}/return          | POST     | 归还                          |
| /api/borrow-records/{id}/rollback-return | POST     | 撤销归还操作                  |
| /api/hot-search                          | GET      | 获取热搜关键词                |
| /api/ranks/borrow                        | GET      | 借阅量排名                    |
| /api/ranks/favorite                      | GET      | 收藏量排名                    |
| /api/ranks/new-monthly                   | GET      | 近 30 天首次入库的图书        |
| /api/ranks/new-store                     | GET      | 近期新增馆藏                  |
| /api/dashboard                           | GET      | 获取数据概览数据              |

备注：
- 加 `*` 的接口，需要在请求时附带图片验证码相关参数。
- 加 `+` 的接口，需要在请求时附带邮箱验证码相关参数。