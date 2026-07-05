package pers.liaohaolong.mokulibserver.service.business;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <h3>邮箱密码信息加载服务</h3>
 *
 * @see UserDetailsService
 */
public interface EmailPasswordDetailsService extends UserDetailsService {

    @NonNull UserDetails loadUserPasswordByEmail(@NonNull String email) throws UsernameNotFoundException;

    default @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return loadUserPasswordByEmail(username);
    }

}
