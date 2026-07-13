package pers.liaohaolong.mokulibserver.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.liaohaolong.mokulibserver.model.ActivationToken;

import java.io.Serializable;

public interface ActivationTokenMapper extends BaseMapper<ActivationToken> {

    default ActivationToken selectByToken(Serializable token) {
        return selectOne(new LambdaQueryWrapper<ActivationToken>().eq(ActivationToken::getToken, token));
    }

}
