package authorization.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 令牌增强器
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-13 下午11:26
 */
//@Component
public class InfoTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // 创建一个自定义信息
        Map<String, Object> additionalInfo = new HashMap<>(1);
        // 设置值
        additionalInfo.put("organization", authentication.getName());
        // 存进去
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        // 返回
        return accessToken;
    }
}
