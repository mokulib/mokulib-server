package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jspecify.annotations.NonNull;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;

import java.util.List;

public interface UserService extends IService<User> {

    void uploadAvatar(Integer id, byte[] avatar) throws BusinessException;

    List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException;

    NonsensitiveUserDTO get(@NonNull Integer id) throws BusinessException;

    NonsensitiveUserDTO get(@NonNull String email) throws BusinessException;

}
