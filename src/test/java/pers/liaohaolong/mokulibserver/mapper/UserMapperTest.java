package pers.liaohaolong.mokulibserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.liaohaolong.mokulibserver.MokuLibServerTest;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.model.User;

@Slf4j
@MokuLibServerTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Order(1)
    @DisplayName("插入测试")
    public void insertTest() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123456");
        user.setUsername("test");

        userMapper.insert(user);

        Assertions.assertNotNull(user.getId());
    }

    @Test
    @Order(2)
    @DisplayName("查询测试")
    public void selectTest() {
        User user = userMapper.selectById(4);

        Assertions.assertNotNull(user);
    }

    @Test
    @Order(3)
    @DisplayName("非唯一邮箱查询测试，应能自动过滤 isDeleted 字段")
    public void selectTestByEmail() {
        User user = userMapper.selectByEmail("mr_wang@example.com");

        Assertions.assertNotNull(user);

        log.info("user={}", user);
    }

}
