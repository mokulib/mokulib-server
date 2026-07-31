package pers.liaohaolong.mokulibserver.service.business;

import org.jspecify.annotations.NonNull;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;

import java.util.List;

public interface UserService {

    void uploadAvatar(Integer id, byte[] avatar) throws BusinessException;

    List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException;

    User get(@NonNull Integer id) throws BusinessException;

    User get(@NonNull String email) throws BusinessException;

}
