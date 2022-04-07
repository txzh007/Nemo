package link.tanxin.auth.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author tan
 */


@RestController
@Slf4j
public class IndexController {
    @GetMapping("index")
    public Mono<String> index() {

        Mono<String> mono = Mono.just("hello world!");
        return mono;
    }
}
