package authorization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-14 下午3:18
 */
@Data
@Configuration
@ConfigurationProperties("application.security.oauth")
public class SecurityProperties {

    /**
     * 登录请求的路径, 默认值 /authorization/form
     */
    private String loginProcessingUrl = "/authorization/form";

}
