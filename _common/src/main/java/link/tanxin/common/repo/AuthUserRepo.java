package link.tanxin.common.repo;

import link.tanxin.common.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tan
 */
public interface AuthUserRepo extends JpaRepository<AuthUser, Long> {
}
