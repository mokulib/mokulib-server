package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.ActivationTokenMapper;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.model.ActivationToken;
import pers.liaohaolong.mokulibserver.service.business.ActivationTokenService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationTokenServiceImpl implements ActivationTokenService {

    private final ActivationTokenMapper activationTokenMapper;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public void clearExpired() {
        // 查询过期的激活码
        List<ActivationToken> activationTokens = activationTokenMapper.selectList(new LambdaQueryWrapper<ActivationToken>()
                .lt(ActivationToken::getExpireTime, LocalDateTime.now())
        );
        activationTokens.forEach(activationToken -> {
            // 删除过期的激活码
            activationTokenMapper.deleteById(activationToken.getUserId());
            // 软删除未激活的用户
            userMapper.deleteById(activationToken.getUserId());
        });
    }

}
