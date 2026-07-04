package pers.liaohaolong.mokulibserver.security.authentication.dao;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.RegexpConfigurations;
import pers.liaohaolong.mokulibserver.service.EmailPasswordDetailsService;

import java.util.Objects;

/**
 * <h3>邮箱密码验证提供器</h3>
 *
 * <p>本组件会自动替代默认的 {@link DaoAuthenticationProvider}。</p>
 */
@Component
public class DaoAuthenticationProviderWrapper extends DaoAuthenticationProvider {

    @Autowired
    public DaoAuthenticationProviderWrapper(EmailPasswordDetailsService emailPasswordDetailsService, PasswordEncoder passwordEncoder) {
        super(emailPasswordDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @NullUnmarked
    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        if (!Objects.requireNonNull(authentication.getCredentials()).toString().matches(RegexpConfigurations.PASSWORD_REGEXP))
            return null; // 如果凭据不符合密码正则表达式，跳过本验证器

        return super.authenticate(authentication);
    }
    
}
