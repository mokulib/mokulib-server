package pers.liaohaolong.mokulibserver.service.sercurity;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pers.liaohaolong.mokulibserver.exception.EmailCaptchaNotFoundException;

/**
 * <h3>邮箱验证码信息加载服务</h3>
 *
 * <p>本类的功能对标 {@link EmailPasswordDetailsService}。</p>
 * <p>与邮箱密码信息加载服务的区别是：本类返回的用户信息中，密码字段中应为验证码</p>
 */
public interface EmailCaptchaDetailsService {

    @NonNull UserDetails loadUserCaptchaByEmail(@NonNull String email) throws UsernameNotFoundException, EmailCaptchaNotFoundException;

}
