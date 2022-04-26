package link.tanxin.common.repo;

import link.tanxin.common.entity.AuthUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author tan
 */
public interface AuthUserRepo extends ReactiveCrudRepository<AuthUser, Long> {
    /**
     * 根据用户名、状态查找用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Mono<AuthUser> findAuthUserByUsername(String username);

}
