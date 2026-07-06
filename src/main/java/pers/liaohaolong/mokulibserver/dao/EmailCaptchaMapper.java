package pers.liaohaolong.mokulibserver.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;

public interface EmailCaptchaMapper extends BaseMapper<EmailCaptcha> {

    @Override
    default boolean insertOrUpdate(EmailCaptcha entity) {
        EmailCaptcha emailCaptcha = selectOne(new LambdaQueryWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, entity.getUserId())
                .eq(EmailCaptcha::getBusinessType, entity.getBusinessType())
        );
        if (emailCaptcha == null) {
            return insert(entity) > 0;
        }
        return update(new LambdaUpdateWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, entity.getUserId())
                .eq(EmailCaptcha::getBusinessType, entity.getBusinessType())
                .set(EmailCaptcha::getCaptcha, entity.getCaptcha())
                .set(EmailCaptcha::getIsUsed, entity.getIsUsed())
                .set(EmailCaptcha::getCoolingTime, entity.getCoolingTime())
                .set(EmailCaptcha::getExpireTime, entity.getExpireTime())
        ) > 0;
    }

}
