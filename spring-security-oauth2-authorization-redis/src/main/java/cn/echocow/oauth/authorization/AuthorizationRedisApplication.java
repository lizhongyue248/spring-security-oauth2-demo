package cn.echocow.oauth.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-13 下午6:12
 */
@SpringBootApplication
public class AuthorizationRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationRedisApplication.class, args);
    }
}
