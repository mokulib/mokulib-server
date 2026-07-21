# MokuLib API Design

| API                             | Note                    |
|---------------------------------|-------------------------|
| GET /api/captcha                | 请求图片验证码          |
| GET /api/auth/ping              | 校验认证凭据            |
| * GET /api/auth/login           | 请求邮箱验证码·登录     |
| * POST /api/auth/login          | 邮箱密码登录/注册       |
| POST /api/auth/login            | 邮箱验证码登录          |
| POST /api/auth/activate/{token} | 激活账户                |
| GET /api/auth/forgot-password   | 请求邮箱验证码·忘记密码 |
| POST /api/auth/forgot-password  | 重置密码                |
| GET /api/auth/close-account     | 请求邮箱验证码·关闭账户 |
| POST /api/auth/close-account    | 关闭账户                |

备注：加 `*` 的接口，需要附带图片验证码相关参数。