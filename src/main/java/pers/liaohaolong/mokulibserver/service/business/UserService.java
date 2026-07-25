package pers.liaohaolong.mokulibserver.service.business;

import org.jspecify.annotations.NonNull;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;

import java.util.List;

public interface UserService {

    List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException;

}
