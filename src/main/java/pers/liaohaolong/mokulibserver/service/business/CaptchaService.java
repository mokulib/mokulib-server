package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;

import java.io.IOException;

public interface CaptchaService {

    GetCaptchaDTO getCaptcha() throws IOException;

    /**
     * 清理过期验证码
     */
    void clearExpired();

}
