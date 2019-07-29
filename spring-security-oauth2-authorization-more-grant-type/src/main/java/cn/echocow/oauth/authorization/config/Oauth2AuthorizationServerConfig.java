package cn.echocow.oauth.authorization.config;

import cn.echocow.oauth.authorization.auth.SmsTokenGranter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * oauth2 授权服务器配置
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-27 下午9:59
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final @NonNull AuthenticationManager authenticationManager;
    private final @NonNull UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("oauth2")
                    .secret("$2a$10$uLCAqDwHD9SpYlYSnjtrXemXtlgSvZCNlOwbW/Egh0wufp93QjBUC")
                    .resourceIds("oauth2")
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "sms")
                    .authorities("ROLE_ADMIN", "ROLE_USER")
                    .scopes("all")
                    .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .redirectUris("http://example.com")
                .and()
                .withClient("test")
                    .secret("$2a$10$wlgcx61faSJ8O5I4nLiovO9T36HBQgh4RhOQAYNORCzvANlInVlw2")
                    .resourceIds("oauth2")
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "sms")
                    .authorities("ROLE_ADMIN", "ROLE_USER")
                    .scopes("all")
                    .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .redirectUris("http://example.com");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(this.authenticationManager);
        endpoints.tokenGranter(tokenGranter(endpoints));
    }

    /**
     * 重点
     * 先获取已经有的五种授权，然后添加我们自己的进去
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     * @return TokenGranter
     */
    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        granters.add(new SmsTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), userDetailsService));
        return new CompositeTokenGranter(granters);
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
                .checkTokenAccess("isAuthenticated()");
    }

}
