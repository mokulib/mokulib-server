package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.EmailPasswordDetailsService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailPasswordDetailsServiceImpl implements EmailPasswordDetailsService {

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserDetails loadUserPasswordByEmail(@NonNull String email) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            log.debug("User not found: {}", email);
            throw new UsernameNotFoundException("User not found");
        }
        log.debug("User found: {}", user);
        return user;
    }

}
