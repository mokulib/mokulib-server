package pers.liaohaolong.mokulibserver.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.liaohaolong.mokulibserver.MokuLibServerTest;

@Slf4j
@MokuLibServerTest
public class ConfigurationsTest {

    @Autowired
    private ImagePathConfigurations imagePathConfigurations;

    @Autowired
    private JwtConfigurations jwtConfigurations;

    @Test
    @DisplayName("读取配置测试")
    void read() {
        log.info("imagePathConfigurations={}", imagePathConfigurations);
        log.info("jwtConfigurations={}", jwtConfigurations);
    }

}
