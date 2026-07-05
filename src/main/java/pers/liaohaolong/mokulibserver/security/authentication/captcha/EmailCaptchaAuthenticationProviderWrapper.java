package pers.liaohaolong.mokulibserver.security.authentication.captcha;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import pers.liaohaolong.mokulibserver.config.RegexpConfigurations;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaDetailsService;

import java.util.Objects;

/**
 * <h3>邮箱验证码认证提供器包装器</h3>
 *
 * <p>在 Spring Security 中手动注册本组件。</p>
 */
public class EmailCaptchaAuthenticationProviderWrapper extends EmailCaptchaAuthenticationProvider {

    public EmailCaptchaAuthenticationProviderWrapper(EmailCaptchaDetailsService emailCaptchaDetailsService) {
        super(emailCaptchaDetailsService);
    }

    @Override
    public Authentication authenticate(@NotNull Authentication authentication) throws AuthenticationException {
        if (!Objects.requireNonNull(authentication.getCredentials()).toString().matches(RegexpConfigurations.EMAIL_CAPTCHA_REGEXP))
            return null; // 如果凭据不符合验证码正则表达式，跳过本验证器

        return super.authenticate(authentication);
    }

}
