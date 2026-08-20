package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.dto.request.ResetPasswordDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;

public interface AuthService extends IService<User> {

    GetEmailCaptchaResultDTO getLoginCaptcha(String email) throws BusinessException;

    void activate(String token) throws BusinessException;

    GetEmailCaptchaResultDTO getCloseAccountCaptcha(User user);

    void closeAccount(User user, String captcha) throws BusinessException;

    GetEmailCaptchaResultDTO getResetPasswordCaptcha(User user);

    void resetPassword(User user, String emailCaptcha, ResetPasswordDTO resetPasswordDTO);

}
