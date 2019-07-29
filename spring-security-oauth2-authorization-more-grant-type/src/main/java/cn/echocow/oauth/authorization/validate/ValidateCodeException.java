package cn.echocow.oauth.authorization.validate;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午10:34
 */
public class ValidateCodeException extends RuntimeException {
    public ValidateCodeException(String message) {
        super(message);
    }
}
