package pers.liaohaolong.mokulibserver.security.authentication.captcha;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import pers.liaohaolong.mokulibserver.exception.EmailCaptchaNotFoundException;

/**
 * <h3>抽象的邮箱验证码认证提供器</h3>
 *
 * <p>模仿 {@link org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider} 的实现方式。</p>
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractEmailCaptchaAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**
     * <h3>额外的认证检查</h3>
     *
     * <p>该方法用于验证传入的验证码是否正确。</p>
     *
     * @param userDetails 通过数据库查询到的用户信息，包含本次期望输入的验证码
     * @param authentication 本次请求验证的输入信息，包含用户实际输入的验证码
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
     */
    @SuppressWarnings("SameParameterValue")
    protected abstract void additionalAuthenticationChecks(UserDetails userDetails,
                                                           UsernamePasswordAuthenticationToken authentication) throws AuthenticationException;

    /**
     * <h3>认证用户</h3>
     *
     * <p>该方法负责认证用户，通过邮箱和验证码进行验证。</p>
     * <p>较原版认证过程，将用户名调整为邮箱，省略用户缓存及缓存匹配部分。</p>
     *
     * @param authentication the authentication request object.
     * @return 完整的用户信息
     * @throws AuthenticationException 验证过程中出现的异常
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#authenticate(Authentication)
     */
    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Only UsernamePasswordAuthenticationToken is supported");
        String email = determineEmail(authentication);
        UserDetails user;
        try {
            // 根据传入邮箱查询用户信息
            // 其中，密码字段由本次期望输入的验证码替代
            user = retrieveUser(email, (UsernamePasswordAuthenticationToken) authentication);
        } catch (UsernameNotFoundException | EmailCaptchaNotFoundException ex) {
            // hideUserNotFoundExceptions
            throw new BadCredentialsException("Bad credentials");
        }
        Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");

        preAuthenticationChecks.check(user);
        // 验证传入验证码
        additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        postAuthenticationChecks.check(user);

        return createSuccessAuthentication(user, authentication, user);
    }

    /**
     * <h3>确定邮箱</h3>
     *
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
     */
    private String determineEmail(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    }

    /**
     * <p>创建认证成功后，完整的认证信息</p>
     *
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
     */
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(principal,
                authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        log.debug("Authenticated user");
        return result;
    }

    /**
     * <h3>检索用户</h3>
     *
     * @param email 用户邮箱
     * @param authentication the authentication request object.
     * @return 完整的用户信息
     * @throws AuthenticationException 搜索过程中出现的异常，如未找到用户等
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
     */
    protected abstract UserDetails retrieveUser(String email, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException;

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
     */
    @Slf4j
    private static class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                log.debug("Failed to authenticate since user account is locked");
                throw new LockedException("User account is locked");
            }
            if (!user.isEnabled()) {
                log.debug("Failed to authenticate since user account is disabled");
                throw new DisabledException("User is disabled");
            }
            if (!user.isAccountNonExpired()) {
                log.debug("Failed to authenticate since user account has expired");
                throw new AccountExpiredException("User account has expired");
            }
        }

    }

    /**
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
     */
    @Slf4j
    private static class DefaultPostAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("Failed to authenticate since user account credentials have expired");
                throw new CredentialsExpiredException("User credentials have expired");
            }
        }

    }

}
