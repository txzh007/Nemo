package link.tanxin.auth.service;

import link.tanxin.common.entity.AuthUser;
import link.tanxin.common.exception.BusinessException;
import link.tanxin.common.repo.AuthUserRepo;
import link.tanxin.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限业务层
 *
 * @author tan
 */
@Service
@Slf4j
public class AuthService {

    private final AuthUserRepo authUserRepo;

    public AuthService(AuthUserRepo authUserRepo) {

        this.authUserRepo = authUserRepo;
    }

    public Mono<String> doLogin(String username, String password) {
        Mono<String> mono = null;
        try {
            mono = authUserRepo.findAuthUserByUsername(username).map(item -> {
                Map<String, Object> map = new HashMap<>(1);
                String md5Password = DigestUtils.md5DigestAsHex((JwtUtil.PASSWORDSALT + password).getBytes(StandardCharsets.UTF_8));
                if (md5Password.equals(item.getPassword())) {
                    map.put("id", item.getId());
                    return JwtUtil.createToken(map);
                } else {
                    throw new BusinessException(HttpStatus.UNAUTHORIZED,"登录失败");
                }
            });
        } catch (Exception e) {
            log.error("{1}", e);
        }
        return mono;
    }

}
