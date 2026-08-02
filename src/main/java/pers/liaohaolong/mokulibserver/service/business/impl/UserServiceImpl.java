package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
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
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final ImageService imageService;

    @Override
    public void uploadAvatar(Integer id, byte[] avatar) throws BusinessException {
        imageService.save(ImageConfigurations.ImageType.AVATARS, String.valueOf(id), avatar);
    }

    @Override
    public List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException {
        return userMapper.selectByIds(ids.stream().distinct().toList()).stream().map(user -> new UsernameDTO(user.getId(), user.getUsername())).toList();
    }

    @Override
    public NonsensitiveUserDTO get(@NonNull Integer id) throws BusinessException {
        User user = userMapper.selectById(id);

        if (user == null)
            throw new BusinessException("用户不存在");

        return NonsensitiveUserDTO.fromUser(user);
    }

    @Override
    public NonsensitiveUserDTO get(@NonNull String email) throws BusinessException {
        User user = userMapper.selectByEmail(email);

        if (user == null)
            throw new BusinessException("用户不存在");

        return NonsensitiveUserDTO.fromUser(user);
    }

}
