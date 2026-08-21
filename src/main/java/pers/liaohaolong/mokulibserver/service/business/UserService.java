package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jspecify.annotations.NonNull;
import pers.liaohaolong.mokulibserver.dto.response.BorrowingDTO;
import pers.liaohaolong.mokulibserver.dto.response.HistoryDTO;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.User;

import java.util.List;

public interface UserService extends IService<User> {

    NonsensitiveUserDTO get(@NonNull Integer id) throws BusinessException;

    NonsensitiveUserDTO get(@NonNull String email) throws BusinessException;

    void uploadAvatar(Integer id, byte[] avatar) throws BusinessException;

    void updateUsername(Integer id, String username) throws BusinessException;

    BorrowingDTO getBorrowing(@NonNull Integer id);

    List<Book> getFavorites(@NonNull Integer id);

    List<HistoryDTO> getHistory(@NonNull Integer id);

}
