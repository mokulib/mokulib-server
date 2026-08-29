package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.ActivationTokenMapper;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.ActivationToken;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha.BusinessType;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.base.EmailCaptchaBaseService;
import pers.liaohaolong.mokulibserver.service.business.AuthService;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    private final EmailCaptchaBaseService emailCaptchaBaseService;
    private final ActivationTokenMapper activationTokenMapper;

    @Override
    @Transactional
    public GetEmailCaptchaResultDTO getLoginCaptcha(String email) throws BusinessException {
        // 查询用户
        User user = getBaseMapper().selectByEmail(email);
        // 用户检查
        if (user == null)
            throw new BusinessException("用户不存在");
        if (!user.getIsActivated())
            throw new BusinessException("账户未激活，请先激活账户");

        return emailCaptchaBaseService.getEmailCaptcha(user.getId(), email, BusinessType.LOGIN);
    }

    @Override
    @Transactional
    public void activate(String token) throws BusinessException {
        ActivationToken activationToken = activationTokenMapper.selectByToken(token);
        // 有效验证
        if (activationToken == null)
            throw new BusinessException("链接失效，激活失败");
        // 过期验证
        if (LocalDateTime.now().isAfter(activationToken.getExpireTime())) {
            throw new BusinessException("链接失效，激活失败");
        }

        // 查询需要激活的账户
        User user = getById(activationToken.getUserId());
        // 账户检查（是否已注销）
        if (user == null)
            throw new BusinessException("链接失效，激活失败");
        // 账户检查（是否已激活）
        if (user.getIsActivated())
            throw new BusinessException("链接失效，激活失败");

        // 用户存在，且未激活，激活账户
        update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, activationToken.getUserId())
                .set(User::getIsActivated, true)
        );
    }

}
