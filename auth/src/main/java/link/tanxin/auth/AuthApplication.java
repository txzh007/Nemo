package link.tanxin.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 权限子模块
 *
 * @author tan
 */
@EnableDiscoveryClient
@EnableWebFlux
@SpringBootApplication
@EnableR2dbcRepositories
@ComponentScan(basePackages = {"link.tanxin.**"})
public class AuthApplication {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(AuthApplication.class, args);
//        displayAllBeans();
    }

    /**
     * 打印所以装载的bean
     */
    public static void displayAllBeans() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : allBeanNames) {
            System.out.println(beanName);
        }
    }


}


