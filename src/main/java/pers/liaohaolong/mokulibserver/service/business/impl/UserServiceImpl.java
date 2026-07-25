package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.service.business.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException {
        return userMapper.selectByIds(ids.stream().distinct().toList()).stream().map(user -> new UsernameDTO(user.getId(), user.getUsername())).toList();
    }

}
