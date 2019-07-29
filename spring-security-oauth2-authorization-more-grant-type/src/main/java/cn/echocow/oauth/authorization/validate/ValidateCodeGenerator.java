package cn.echocow.oauth.authorization.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午10:17
 */
public interface ValidateCodeGenerator {
    /**
     * 生成验证码
     *
     * @param request 请求
     * @return 生成结果
     */
    String generate(ServletWebRequest request);
}
