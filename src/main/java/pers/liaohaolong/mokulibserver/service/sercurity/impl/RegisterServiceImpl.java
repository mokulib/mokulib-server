package pers.liaohaolong.mokulibserver.service.sercurity.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import pers.liaohaolong.mokulibserver.dao.ActivationTokenMapper;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.model.ActivationToken;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.base.MailService;
import pers.liaohaolong.mokulibserver.service.sercurity.RegisterService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    public static final List<String> ADJECTIVES = List.of("快乐", "勇敢", "聪明", "可爱", "帅气", "温柔", "酷酷", "萌萌", "呆呆", "调皮", "安静", "活泼", "阳光", "神秘", "奇幻", "魔法", "甜蜜", "幸福", "幸运", "灿烂", "闪耀", "飞翔", "自由");
    public static final List<String> NOUNS = List.of("小猫", "小狗", "小兔", "小熊", "老虎", "狮子", "鲸鱼", "海豚", "鹰", "凤凰", "龙", "麒麟", "精灵", "天使", "骑士", "武士", "法师", "猎人", "诗人", "画家", "星星", "月亮", "太阳", "云朵");

    private final UserMapper userMapper;
    private final ActivationTokenMapper activationTokenMapper;
    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(@NonNull Authentication authentication) {
        assert authentication.getCredentials() != null;

        String email = determineUsername(authentication);
        String password = authentication.getCredentials().toString();

        log.info("用户注册：邮箱={}, 密码={}", email, password);

        // 保存用户
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(pickRandom(ADJECTIVES) + pickRandom(NOUNS));
        userMapper.insert(user);

        user = userMapper.selectByEmail(email);

        // 使用用户 ID 创建激活码
        ActivationToken activationToken = new ActivationToken();
        activationToken.setUserId(user.getId());
        activationToken.setToken(UUID.randomUUID().toString().replace("-", ""));
        activationToken.setExpireTime(user.getCreateTime().plusDays(7));
        activationTokenMapper.insert(activationToken);

        activationToken = activationTokenMapper.selectById(user.getId());

        // 发送邮件
        Context context = new Context();
        context.setVariable("activationCode", activationToken.getToken());
        context.setVariable("textExpiredAt", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(activationToken.getExpireTime()));
        mailService.sendMail(email, "激活您的账户", "register", context);

        log.debug("发送激活邮件：邮箱={}, 激活码={}", email, activationToken.getToken());
    }

    private String determineUsername(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    }

    private String pickRandom(List<String> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

}
