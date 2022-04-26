package link.tanxin.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 服务异常类
 * @author tan
 */
public class BusinessException extends ResponseStatusException {
    public BusinessException(HttpStatus status, String message) {
        super(status, message);
    }

    public BusinessException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}
