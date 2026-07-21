package pers.liaohaolong.mokulibserver.security.authentication.dao;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.RegexpConfigurations;
import pers.liaohaolong.mokulibserver.service.sercurity.EmailPasswordDetailsService;
import pers.liaohaolong.mokulibserver.service.sercurity.RegisterService;

import java.util.Objects;

/**
 * <h3>邮箱密码验证提供器包装器</h3>
 *
 * <p>本组件会自动替代默认的 {@link DaoAuthenticationProvider}。</p>
 * <p>本组件整合了注册接口，当验证过程中抛出用户未找到异常时，进入注册逻辑。</p>
 */
@Component
public class DaoAuthenticationProviderWrapper extends DaoAuthenticationProvider {

    private final RegisterService registerService;

    @Autowired
    public DaoAuthenticationProviderWrapper(EmailPasswordDetailsService emailPasswordDetailsService, PasswordEncoder passwordEncoder, RegisterService registerService) {
        super(emailPasswordDetailsService);
        this.setPasswordEncoder(passwordEncoder);
        this.setHideUserNotFoundExceptions(false);
        this.registerService = registerService;
    }

    @NullUnmarked
    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        if (!Objects.requireNonNull(authentication.getCredentials()).toString().matches(RegexpConfigurations.PASSWORD_REGEXP))
            return null; // 如果凭据不符合密码正则表达式，跳过本验证器

        try {
            // 尝试登录
            return super.authenticate(authentication);
        } catch (UsernameNotFoundException e) {
            // 邮箱不存在时，转入注册服务
            registerService.register(authentication);
            // 注册服务沿用 UsernameNotFoundException 语义，重新抛出 UsernameNotFoundException
            throw e;
        }
    }
    
}
