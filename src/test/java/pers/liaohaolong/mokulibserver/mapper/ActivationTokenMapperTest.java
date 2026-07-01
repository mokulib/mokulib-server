package pers.liaohaolong.mokulibserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.liaohaolong.mokulibserver.MokuLibServerTest;
import pers.liaohaolong.mokulibserver.dao.ActivationTokenMapper;
import pers.liaohaolong.mokulibserver.model.ActivationToken;

import java.time.LocalDateTime;

@Slf4j
@MokuLibServerTest
public class ActivationTokenMapperTest {

    @Autowired
    private ActivationTokenMapper activationTokenMapper;

    @Test
    @Order(1)
    @DisplayName("插入测试")
    void insertTest() {
        ActivationToken activationToken = new ActivationToken();
        activationToken.setUserId(1);
        activationToken.setToken("test-token");
        activationToken.setExpireTime(LocalDateTime.now().plusHours(1));

        activationTokenMapper.insert(activationToken);

        Assertions.assertEquals(1, activationToken.getUserId());
    }

    @Test
    @Order(2)
    @DisplayName("查询测试")
    void selectTest() {
        ActivationToken activationToken = activationTokenMapper.selectById(1);

        Assertions.assertNotNull(activationToken);

        log.info("activationToken={}", activationToken);
    }

}
