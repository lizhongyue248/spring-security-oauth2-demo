package cn.echocow.oauth.authorization;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 获取加密后的密码
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-13 下午4:36
 */
public class PasswordTest {

    @Test
    public void password() {
        System.out.println(new BCryptPasswordEncoder().encode("oauth2"));
    }

}
