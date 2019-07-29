package cn.echocow.oauth.authorization.validate.impl;

import cn.echocow.oauth.authorization.validate.ValidateCodeException;
import cn.echocow.oauth.authorization.validate.ValidateCodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * redis 验证码操作
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午10:44
 */
@Component
@RequiredArgsConstructor
public class ValidateCodeRepositoryImpl implements ValidateCodeRepository {

    private final @NonNull RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(ServletWebRequest request, String code, String type) {
        redisTemplate.opsForValue().set(buildKey(request, type), code,
                //  有效期可以从配置文件中读取或者请求中读取
                Duration.ofMinutes(10).getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public String get(ServletWebRequest request, String type) {
        return redisTemplate.opsForValue().get(buildKey(request, type));
    }

    @Override
    public void remove(ServletWebRequest request, String type) {
        redisTemplate.delete(buildKey(request, type));
    }

    /**
     * 构建 redis 存储时的 key
     *
     * @param request 请求
     * @param type 类型
     * @return key
     */
    private String buildKey(ServletWebRequest request, String type) {
        String deviceId = request.getParameter(type);
        if (StringUtils.isEmpty(deviceId)) {
            throw new ValidateCodeException("请求中不存在 " + type);
        }
        return "code:" + type + ":" + deviceId;
    }
}
