package pers.liaohaolong.mokulibserver.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.RegexpConfigurations;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, @NotNull HttpServletResponse response, @NonNull AuthenticationException exception) throws IOException {
        log.debug("Authentication Failure Handler");

        // 准备响应
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        // 根据登录方式决定响应数据
        String password = request.getParameter("password");
        ResultDTO result = null;

        // 在本项目的邮箱密码登录方式中，可能抛出 UsernameNotFoundException、BadCredentialsException 或 LockedException
        if (password.matches(RegexpConfigurations.PASSWORD_REGEXP)) {
            if (exception instanceof UsernameNotFoundException)
                result = createFailureResult("注册成功！激活邮件已发送，点击链接即可激活账户。");
            if (exception instanceof BadCredentialsException)
                result = createFailureResult("密码错误");
            else if (exception instanceof LockedException)
                result = createFailureResult("账户未激活，请先激活账户");
        }
        // 在本项目的邮箱验证码登录方式中，可能抛出 BadCredentialsException、LockedException
        else if (password.matches(RegexpConfigurations.EMAIL_CAPTCHA_REGEXP)) {
            if (exception instanceof BadCredentialsException) // 包括 UsernameNotFoundException, EmailCaptchaNotFoundException（含验证码过期和验证码已使用）和 BadCredentialsException
                result = createFailureResult("邮箱不存在、验证码错误或验证码已过期");
            else if (exception instanceof LockedException)
                result = createFailureResult("账户未激活，请先激活账户");
        }
        // 均不匹配的情况已由 InvalidLoginRequestFilter 过滤器拦截，此处不应出现
        assert result != null;

        // 输出响应
        response.getWriter().write(result.toJson(objectMapper));
        response.getWriter().flush();
    }

    private ResultDTO createFailureResult(@NonNull String message) {
        return ResultDTO.error().businessType("LOGIN").message(message).build();
    }

}
