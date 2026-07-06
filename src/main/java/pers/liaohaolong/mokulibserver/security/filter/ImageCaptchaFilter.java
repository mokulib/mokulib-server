package pers.liaohaolong.mokulibserver.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.liaohaolong.mokulibserver.config.LoginFilterConfigurations;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.service.business.ImageCaptchaService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCaptchaFilter extends OncePerRequestFilter {

    /**
     * 需要进行验证码验证的请求匹配器列表
     */
    private static final List<RequestMatcher> requiresAuthenticationRequestMatchers = List.of(
            LoginFilterConfigurations.PASSWORD_LOGIN_REQUEST_MATCHER, // 密码登录
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/auth/login"), // 邮箱验证码登录的请求邮箱验证码接口
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/auth/register") // 注册账户
    );

    private final ImageCaptchaService imageCaptchaService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (requiresAuthenticationRequestMatchers.stream().anyMatch(requestMatcher -> requestMatcher.matches(request))) {
            String token = request.getParameter("captchaToken");
            String captcha = request.getParameter("captcha");

            // 验证失败
            if (!imageCaptchaService.verify(token, captcha)) {
                // response.sendError(HttpServletResponse.SC_BAD_REQUEST); // 不要使用这种方法，这种方法会将请求转发到 /error
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(ResultDTO.error().businessType("验证码").message("验证码错误，请重新输入").build().toJson());
                response.getWriter().flush(); // 确保响应立即发送
                return; // 直接返回，不继续执行后续的过滤器
            }
        }

        filterChain.doFilter(request, response);
    }

}
