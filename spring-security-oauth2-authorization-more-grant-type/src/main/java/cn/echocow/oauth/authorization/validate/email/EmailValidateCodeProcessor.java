package cn.echocow.oauth.authorization.validate.email;

import cn.echocow.oauth.authorization.validate.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 邮箱验证码处理器
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午10:00
 */
@Component
public class EmailValidateCodeProcessor extends AbstractValidateCodeProcessor {

    @Override
    protected void send(ServletWebRequest request, String validateCode) {
        System.out.println(request.getParameter("email") +
                "邮箱验证码发送成功，验证码为：" + validateCode);
    }

}
