package cn.echocow.oauth.authorization.validate.email;

import cn.echocow.oauth.authorization.util.RandomCode;
import cn.echocow.oauth.authorization.validate.ValidateCodeGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 邮箱验证码生成器
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午10:23
 */
@Component
public class EmailValidateCodeGenerator implements ValidateCodeGenerator {

    @Override
    public String generate(ServletWebRequest request) {
        return RandomCode.random(6);
    }

}
