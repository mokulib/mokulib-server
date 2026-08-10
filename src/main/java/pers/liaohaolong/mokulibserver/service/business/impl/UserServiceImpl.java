package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.base.ImageService;
import pers.liaohaolong.mokulibserver.service.business.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final ImageService imageService;

    @Override
    public void uploadAvatar(Integer id, byte[] avatar) throws BusinessException {
        imageService.save(ImageConfigurations.ImageType.AVATARS, String.valueOf(id), avatar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException {
        return listByIds(ids.stream().distinct().toList()).stream().map(user -> new UsernameDTO(user.getId(), user.getUsername())).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NonsensitiveUserDTO get(@NonNull Integer id) throws BusinessException {
        User user = getById(id);

        if (user == null)
            throw new BusinessException("用户不存在");

        return NonsensitiveUserDTO.fromUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public NonsensitiveUserDTO get(@NonNull String email) throws BusinessException {
        User user = getBaseMapper().selectByEmail(email);

        if (user == null)
            throw new BusinessException("用户不存在");

        return NonsensitiveUserDTO.fromUser(user);
    }

}
