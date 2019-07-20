package authorization.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * oauth2 授权服务器配置
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-13 下午4:11
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final @NonNull AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("oauth2")
                .secret("$2a$10$wlgcx61faSJ8O5I4nLiovO9T36HBQgh4RhOQAYNORCzvANlInVlw2")
                .resourceIds("oauth2")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_ADMIN", "ROLE_USER")
                .scopes("all")
                .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                .redirectUris("http://example.com");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        endpoints.authenticationManager(this.authenticationManager)
//                .tokenStore(tokenStore())
//                .accessTokenConverter(jwtAccessTokenConverter());

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager);
    }

    /**
     * 令牌增强器
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap<>(1);
            additionalInfo.put("organization", authentication.getName());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * 令牌转换器，（非）对称密钥加密
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //  对称密钥加密
        //  converter.setSigningKey("oauth2");
        //  非对称密钥加密
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("oauth2.jks"), "123456".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2"));
        return converter;
    }

    /**
     * token store 实现
     *
     * @return JwtTokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 资源服务器所需，后面会讲
     * 具体作用见本系列的第二篇文章授权服务器最后一部分
     * 具体原因见本系列的第三篇文章资源服务器
     *
     * @param security security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // 能够验证和解析 token
                .checkTokenAccess("isAuthenticated()")
                // 能够访问我们的公钥
                .tokenKeyAccess("isAuthenticated()");
    }
}
