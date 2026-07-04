package pers.liaohaolong.mokulibserver.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.liaohaolong.mokulibserver.security.matcher.RegexpRequestHeaderRequestMatcher;
import pers.liaohaolong.mokulibserver.util.JwtUtils;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final RequestMatcher MATCHER = new RegexpRequestHeaderRequestMatcher(AUTHORIZATION_HEADER, "Bearer .*");

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtRequestFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * 若请求头中包含 JWT，则提取 JWT 并验证。<br>
     * 若 JWT 有效则创建认证信息并设置到 SecurityContextHolder 中，继续执行过滤器链；<br>
     * 若 JWT 无效则继续执行过滤器链。
     *
     * @param request request
     * @param response response
     * @param filterChain filterChain
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 判断请求头是否包含 JWT
        RequestMatcher.MatchResult matchResult = MATCHER.matcher(request);

        if (matchResult.isMatch()) {
            // 获取请求头
            String authorizationHeader = matchResult.getVariables().get(AUTHORIZATION_HEADER);
            // 提取 JWT
            String jwt = authorizationHeader.substring(7);
            // 验证 JWT
            boolean isValid = false;
            try {
                isValid = !jwtUtils.isTokenExpired(jwt);
            } catch (JwtException | IllegalArgumentException ignore) {
                // 无效或过期
            }

            if (isValid) {
                // 从 JWT 中提取用户信息
                UserDetails userDetails = jwtUtils.extractUserDetails(jwt);
                // 创建认证信息
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 设置请求信息，以便后续的其他 Filter 或 Controller 使用
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 设置认证信息
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
