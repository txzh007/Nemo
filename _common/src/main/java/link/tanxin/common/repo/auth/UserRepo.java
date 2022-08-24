package link.tanxin.common.repo.auth;

import link.tanxin.common.entity.auth.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author tan
 */
public interface UserRepo extends ReactiveCrudRepository<User, Long> {
    /**
     * 根据用户名、状态查找用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Mono<User> findAuthUserByUsername(String username);

}
