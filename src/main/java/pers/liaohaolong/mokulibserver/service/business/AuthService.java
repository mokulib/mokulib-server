package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;

public interface AuthService {

    GetEmailCaptchaResultDTO getLoginCaptcha(String email) throws BusinessException;

    void register(String email, String password, String username) throws BusinessException;

    void activate(String token) throws BusinessException;

    GetEmailCaptchaResultDTO getCloseAccountCaptcha(User user);

    void closeAccount(User user, String captcha) throws BusinessException;

}
