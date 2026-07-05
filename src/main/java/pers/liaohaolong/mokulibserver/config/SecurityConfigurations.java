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
import pers.liaohaolong.mokulibserver.security.CustomAccessDeniedHandler;
import pers.liaohaolong.mokulibserver.security.CustomAuthenticationEntryPoint;
import pers.liaohaolong.mokulibserver.security.authentication.AuthenticationFailureHandler;
import pers.liaohaolong.mokulibserver.security.authentication.AuthenticationSuccessHandler;
import pers.liaohaolong.mokulibserver.security.authentication.captcha.EmailCaptchaAuthenticationProviderWrapper;
import pers.liaohaolong.mokulibserver.security.filter.InvalidLoginRequestFilter;
import pers.liaohaolong.mokulibserver.security.filter.JwtRequestFilter;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaDetailsService;

@Slf4j
@Configuration
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler,
            EmailCaptchaDetailsService emailCaptchaDetailsService,
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

        // 登录
        http.formLogin(configurer -> configurer
                .loginProcessingUrl("/api/auth/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
        );

        // 配置授权规则(包括Mapping接口和需要权限控制的静态资源)
        http.authorizeHttpRequests(registry -> registry
                // 仅管理员访问 Swagger UI
                .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
                // 其余接口放行
                .anyRequest().permitAll()
        );

        // 配置异常处理
        http.exceptionHandling(configurer -> configurer
                // 处理认证失败 401（正常情况下应该登录认证失败 -> 跳转到登录页面）
                .authenticationEntryPoint(authenticationEntryPoint)
                // 处理权限不足 403
                .accessDeniedHandler(accessDeniedHandler)
        );

        // 禁用 session
        http.sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 认证器配置过程（通过Debug得知）：
        // Spring Security 共有五个认证器提供者的提供者（有序），任意一个提供者如果能够提供 AuthenticationProvider，则后续的认证器提供者均不再执行
        // 前三个未深入研究
        // 第四个会自动扫描BeanFactory中的所有 AuthenticationProvider 实现，一旦扫描到，将自动使用（BeanFactory 中有多个实现时，跳过）
        // 第五个为保底，如果前四个均未生成 AuthenticationProvider，则使用默认的 DaoAuthenticationProvider
        // 本项目的 DaoAuthenticationProviderWrapper 会在第四步被扫描到，从而替代第五个保底的 DaoAuthenticationProvider

        // 最终，生成的验证链路结构如下：ProviderManager(
        //     providers=[下方添加的自定义认证器会添加在这里, AnonymousAuthenticationProvider]                           先执行，按顺序执行
        //     parent=ProviderManager(providers=[DaoAuthenticationProvider(使用@Component注解的自定义认证器会替换它)])   以上执行返回为null且未报账户状态或内部异常时(这两个会打断)，才会执行
        // )
        // 以上流程结束后，执行以下的代码，以下代码将插入到上方的配置点处。
        http.authenticationProvider(new EmailCaptchaAuthenticationProviderWrapper(emailCaptchaDetailsService));

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
