# MokuLib API Design

| HttpMethod | Path                       | ImageCaptcha | Note    |
|------------|----------------------------|--------------|---------|
| GET        | /api/captcha               |              | 请求图片验证码 |
| GET        | /api/auth/ping             |              | 校验认证凭据  |
| GET        | /api/auth/login            | Y            | 请求邮箱验证码 |
| POST       | /api/auth/login            | Y            | 邮箱密码登录  |
| POST       | /api/auth/login            |              | 邮箱验证码登录 |
| POST       | /api/auth/register         | Y            | 注册      |
| GET        | /api/auth/activate/{token} |              | 激活账户    |
| GET        | /api/auth/close-account    |              | 请求邮箱验证码 |
| POST       | /api/auth/close-account    |              | 关闭账户    |