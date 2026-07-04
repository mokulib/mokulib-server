package pers.liaohaolong.mokulibserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import pers.liaohaolong.mokulibserver.security.filter.InvalidLoginRequestFilter;
import pers.liaohaolong.mokulibserver.security.filter.JwtRequestFilter;

@Slf4j
@Configuration
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            InvalidLoginRequestFilter invalidLoginRequestFilter,
            JwtRequestFilter jwtRequestFilter
    ) {
        // 关闭 CSRF 防护，因为我们使用 JWT 进行认证，不需要 CSRF 防护
        http.csrf(AbstractHttpConfigurer::disable);

        // 关闭 CORS
        http.cors(configurer -> configurer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("*");
            configuration.addAllowedHeader("*");
            configuration.addAllowedMethod("*");
            return configuration;
        }));

        // 配置授权规则(包括Mapping接口和需要权限控制的静态资源)
        http.authorizeHttpRequests(registry -> registry
                // 仅管理员访问 Swagger UI
                .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
                // 其余接口放行
                .anyRequest().permitAll()
        );

        // 禁用 session
        http.sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 添加过滤器
        http.addFilterBefore(invalidLoginRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
