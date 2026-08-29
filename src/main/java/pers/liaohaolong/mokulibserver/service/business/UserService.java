package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jspecify.annotations.NonNull;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.dto.request.ResetPasswordDTO;
import pers.liaohaolong.mokulibserver.dto.response.BorrowRecordWithBookIdDTO;
import pers.liaohaolong.mokulibserver.dto.response.HistoryDTO;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;

import java.util.List;

public interface UserService extends IService<User> {

    NonsensitiveUserDTO get(@NonNull Integer id) throws BusinessException;

    NonsensitiveUserDTO get(@NonNull String email) throws BusinessException;

    void uploadAvatar(Integer id, byte[] avatar) throws BusinessException;

    void updateUsername(Integer id, String username) throws BusinessException;

    List<BorrowRecordWithBookIdDTO> getBorrowing(@NonNull Integer id);

    List<Integer> getFavorites(@NonNull Integer id);

    List<HistoryDTO> getHistory(@NonNull Integer id);

    GetEmailCaptchaResultDTO getCloseAccountCaptcha(User user);

    void closeAccount(User user, String captcha) throws BusinessException;

    GetEmailCaptchaResultDTO getResetPasswordCaptcha(User user);

    void resetPassword(User user, String emailCaptcha, ResetPasswordDTO resetPasswordDTO);

}
