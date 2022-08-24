package link.tanxin.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT校验工具类
 *
 * @author tan
 */
@Slf4j
@Component
public class JwtUtil {
    /**
     * 过期时间 这里是两小时
     */

    private static Long EXP_TIME;

    /**
     * Token Salt
     */
    private static String SALT;

    @Autowired
    private Environment env;

    private static Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SALT));
    }


    /**
     * 加密生成token
     *
     * @param map 载体信息
     * @return jwt
     */
    public static <T> String createToken(Map<String, Object> map) {
        return Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME))
                .signWith(getKey())
                .compact();
    }

    /**
     * 解析token中Claims
     *
     * @param token 加密后的token字符串
     * @return 用户信息
     */
    public static Map<String, Object> getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (IllegalArgumentException | JwtException e) {
            log.warn("JWT 解析失败 {1}", e);
        }
        return null;
    }

    /**
     * 校验token是否有效
     *
     * @param token jwt 字符串
     * @return 是否有效
     */
    public static Boolean verifyToken(String token) {
        Claims claims = (Claims) getClaims(token);
        if (null != claims) {
            return claims.getExpiration()
                    .after(new Date());
        }
        return false;
    }


    @PostConstruct
    public void init() {
        EXP_TIME = Long.valueOf(env.getProperty("nemo.jwt.expiration-seconds", "72000"));
        SALT = env.getProperty("nemo.jwt.secret-key", "LIUXINTONGILOVEUXXXXXXXXXXXXXXXXXXXXXXT0106163029");
    }
}
