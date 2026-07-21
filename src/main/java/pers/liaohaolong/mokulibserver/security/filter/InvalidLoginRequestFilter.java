package pers.liaohaolong.mokulibserver.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static pers.liaohaolong.mokulibserver.config.LoginFilterConfigurations.*;

/**
 * <h3>无效登录请求过滤器</h3>
 *
 * <p>由于两种登录请求的凭据正则互斥，因此只可能出现匹配其中一种，或者都不匹配的情况。</p>
 */
@Slf4j
@Component
@AllArgsConstructor
public class InvalidLoginRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 是否是登录请求
        if (LOGIN_REQUEST_MATCHER.matches(request)) {
            // 如果是登录请求，则判断是否匹配密码登录请求或验证码登录请求
            if (!PASSWORD_LOGIN_REQUEST_MATCHER.matches(request) && !CAPTCHA_LOGIN_REQUEST_MATCHER.matches(request)) {
                // 如果均不匹配，则返回请求错误
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(ResultDTO.error().message("请求错误").build().toJson(objectMapper));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
