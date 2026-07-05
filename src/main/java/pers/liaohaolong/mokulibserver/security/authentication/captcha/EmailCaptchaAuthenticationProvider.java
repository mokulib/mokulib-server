package pers.liaohaolong.mokulibserver.security.authentication.captcha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pers.liaohaolong.mokulibserver.exception.EmailCaptchaNotFoundException;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaDetailsService;

/**
 * <h3>邮箱验证码认证提供器</h3>
 *
 * 模仿 {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider} 的实现方式。
 */
@Slf4j
public class EmailCaptchaAuthenticationProvider extends AbstractEmailCaptchaAuthenticationProvider {

    private final EmailCaptchaDetailsService emailCaptchaDetailsService;

    public EmailCaptchaAuthenticationProvider(EmailCaptchaDetailsService emailCaptchaDetailsService) {
        this.emailCaptchaDetailsService = emailCaptchaDetailsService;
    }

    /**
     * 比较实际输入的凭据和期望的凭据是否一致
     *
     * @param userDetails 通过数据库查询到的用户信息，包含本次期望输入的验证码
     * @param authentication 本次请求验证的输入信息，包含用户实际输入的验证码
     * @throws AuthenticationException 核验失败时，抛出 {@link BadCredentialsException}
     * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("Bad credentials");
        }
        String presentedCaptcha = authentication.getCredentials().toString();
        if (!presentedCaptcha.equals(userDetails.getPassword())) {
            log.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    protected UserDetails retrieveUser(String email, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = this.emailCaptchaDetailsService.loadUserCaptchaByEmail(email);
            // noinspection ConstantValue
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException | EmailCaptchaNotFoundException | InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

}
