package pers.liaohaolong.mokulibserver.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.liaohaolong.mokulibserver.model.User;

import java.io.Serializable;

public interface UserMapper extends BaseMapper<User> {

    default User selectByEmail(Serializable email) {
        return selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }

}
