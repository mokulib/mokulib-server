package pers.liaohaolong.mokulibserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.JwtConfigurations;
import pers.liaohaolong.mokulibserver.dto.JwtUserDTO;
import pers.liaohaolong.mokulibserver.model.User;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JwtUtils {

    private final SecretKey secretKey;

    private final long expireMillis;

    private final ObjectMapper objectMapper;

    @Autowired
    public JwtUtils(JwtConfigurations jwtConfigurations, ObjectMapper objectMapper) {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfigurations.getSecret().getBytes()); // 配置密钥
        this.expireMillis = jwtConfigurations.getExpireMinutes() * 60 * 1000; // 设置时间
        this.objectMapper = objectMapper;
    }

    // 生成 JWT
    public String generateToken(@NotNull UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("user", objectMapper.writeValueAsString(new JwtUserDTO((User) userDetails)));
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireMillis))
                .signWith(secretKey)
                .compact();
    }

    // 从 JWT 中提取用户
    public UserDetails extractUserDetails(String token) throws JwtException, IllegalArgumentException {
        String user = extractClaim(token, claims -> claims.get("user", String.class));
        return objectMapper.readValue(user, JwtUserDTO.class).toUser();
    }

    // 从 JWT 中提取过期时间
    public Date extractExpiration(String token) throws JwtException, IllegalArgumentException {
        return extractClaim(token, Claims::getExpiration);
    }

    // 检查 JWT 是否过期
    public @NotNull Boolean isTokenExpired(String token) throws JwtException, IllegalArgumentException {
        return extractExpiration(token).before(new Date());
    }

    // 提取 JWT 中的声明
    private <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver) throws JwtException, IllegalArgumentException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析 JWT
    private Claims extractAllClaims(String token) throws JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
