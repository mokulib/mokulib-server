package pers.liaohaolong.mokulibserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 从 application.properties 中读取 jwt 配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mokulib.jwt")
public class JwtConfigurations {

    /**
     * JWT 密钥，HMAC-SHA256 算法要求密钥长度至少为 256 位，即 32 字节
     */
    private String secret;

    /**
     * JWT 过期时间
     */
    private long expireMinutes;

}
