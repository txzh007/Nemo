package link.tanxin.auth.controller;


import link.tanxin.auth.service.AuthService;
import link.tanxin.common.request.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


/**
 * @author tan
 */

@Slf4j
@RestController
@RequestMapping("/passport")
public class PassportController {
    private final AuthService authService;

    PassportController(AuthService authService) {
        this.authService = authService;
    }

    @Data
    private static class LoginForm {
        private String username;
        private String password;
    }

    /**
     * todo:
     *  1. 登录校验
     *  2. 换取token
     */
    @PostMapping(value = "/login")
    public Mono<Result<String>> login(@RequestBody LoginForm loginForm) {
        return authService.doLogin(loginForm.getUsername(), loginForm.getPassword()).map(Result::succeeded);
    }


}
