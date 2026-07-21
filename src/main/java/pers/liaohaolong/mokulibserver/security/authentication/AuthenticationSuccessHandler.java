package pers.liaohaolong.mokulibserver.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.util.JwtUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Authentication authentication) throws IOException {
        log.debug("Authentication Success Handler");

        // 已验证成功
        assert authentication.getPrincipal() instanceof UserDetails;
        // 构建返回对象
        ResultDTO result = ResultDTO.ok()
                .businessType("LOGIN")
                .message("登录成功")
                .data(Map.of("jwt", jwtUtils.generateToken((UserDetails) authentication.getPrincipal())))
                .build();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(result.toJson(objectMapper));
        response.getWriter().flush();
    }

}
