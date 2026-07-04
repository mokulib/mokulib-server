package pers.liaohaolong.mokulibserver.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pers.liaohaolong.mokulibserver.MokuLibServerTest;

@Slf4j
@MokuLibServerTest
public class EncodePasswordTest {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EncodePasswordTest(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void password() {
        String password = "MokuLib2026.";
        String encodedPassword = passwordEncoder.encode(password);

        log.info("原始密码: {}", password);
        log.info("编码后密码: {}", encodedPassword);
    }

}
