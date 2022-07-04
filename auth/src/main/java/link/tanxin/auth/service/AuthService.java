package link.tanxin.auth.service;

import link.tanxin.common.exception.BusinessException;
import link.tanxin.common.repo.AuthUserRepo;
import link.tanxin.common.request.CodeMessage;
import link.tanxin.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public Mono<Object> doLogin(String username, String password) {
        Mono<Object> mono = null;
        try {
            mono = authUserRepo.findAuthUserByUsername(username)
                    .map(item -> {
                        Map<String, Object> map = new HashMap<>(1);
                        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                        boolean matches = passwordEncoder.matches(password, item.getPassword());
                        if (matches) {
                            map.put("id", item.getId());
                            map.put("user", item.getUsername());
                            return JwtUtil.createToken(map);
                        } else {
                            throw new BusinessException(CodeMessage.LOGIN_FAILED, "登录失败");
                        }
                    });
        } catch (Exception e) {
            log.error("登录异常:{ }", e);
        }
        return mono;
    }

}
