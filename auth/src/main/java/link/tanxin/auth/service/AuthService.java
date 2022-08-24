package link.tanxin.auth.service;

import link.tanxin.common.exception.BusinessException;
import link.tanxin.common.repo.auth.UserRepo;
import link.tanxin.common.request.CodeMessage;
import link.tanxin.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
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

    private final UserRepo userRepo;

    public AuthService(UserRepo userRepo) {

        this.userRepo = userRepo;
    }

    public Mono<Object> doLogin(String username, String password) {
        Mono<Object> mono = userRepo.findAuthUserByUsername(username)
                .mapNotNull(item -> {
                    Map<String, Object> map = new HashMap<>(1);
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    boolean matches = passwordEncoder.matches(password, item.getPassword());
                    if (matches) {
                        map.put("id", item.getId());
                        map.put("user", item.getUsername());
                        try {
                            return JwtUtil.createToken(map);
                        } catch (Exception e) {
                            log.error("生成jwt 失败:{ }", e);
                        }
                    } else {
                        throw new BusinessException(CodeMessage.LOGIN_FAILED, "登录失败");
                    }
                    return null;
                });

        return mono;
    }

}
