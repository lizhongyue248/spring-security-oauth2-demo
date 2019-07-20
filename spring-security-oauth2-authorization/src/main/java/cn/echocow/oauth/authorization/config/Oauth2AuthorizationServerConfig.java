package cn.echocow.oauth.authorization.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.time.Duration;

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
    private final @NonNull ClientDetails clientDetails;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
//        builder
//                .withClient("oauth2")
//                .secret("$2a$10$wlgcx61faSJ8O5I4nLiovO9T36HBQgh4RhOQAYNORCzvANlInVlw2")
//                .resourceIds("oauth2")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                .authorities("ROLE_ADMIN", "ROLE_USER")
//                .scopes("all")
//                .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
//                .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
//                .redirectUris("http://example.com");
//        builder
//                .withClient("test")
//                .secret("$2a$10$wlgcx61faSJ8O5I4nLiovO9T36HBQgh4RhOQAYNORCzvANlInVlw2")
//                .resourceIds("test")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                .authorities("ROLE_ADMIN", "ROLE_USER")
//                .scopes("all")
//                .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
//                .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
//                .redirectUris("http://example.com");

        clients.inMemory()
                .withClient("oauth2")
                    .secret("$2a$10$wlgcx61faSJ8O5I4nLiovO9T36HBQgh4RhOQAYNORCzvANlInVlw2")
                    .resourceIds("oauth2")
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                    .authorities("ROLE_ADMIN", "ROLE_USER")
                    .scopes("all")
                    .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .redirectUris("http://example.com")
                .and()
                .withClient("test")
                    .secret("$2a$10$wlgcx61faSJ8O5I4nLiovO9T36HBQgh4RhOQAYNORCzvANlInVlw2")
                    .resourceIds("oauth2")
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                    .authorities("ROLE_ADMIN", "ROLE_USER")
                    .scopes("all")
                    .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                    .redirectUris("http://example.com");

//        configClient(clients);
    }

    private void configClient(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        for (BaseClientDetails client : clientDetails.getClient()) {
            ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder clientBuilder =
                    builder.withClient(client.getClientId());
            clientBuilder
                    .secret(client.getClientSecret())
                    .resourceIds(client.getResourceIds().toArray(new String[0]))
                    .authorizedGrantTypes(client.getAuthorizedGrantTypes().toArray(new String[0]))
                    .authorities(
                            AuthorityUtils.authorityListToSet(client.getAuthorities())
                                    .toArray(new String[0]))
                    .scopes(client.getScope().toArray(new String[0]));
            if (client.getAutoApproveScopes() != null) {
                clientBuilder.autoApprove(
                        client.getAutoApproveScopes().toArray(new String[0]));
            }
            if (client.getAccessTokenValiditySeconds() != null) {
                clientBuilder.accessTokenValiditySeconds(
                        client.getAccessTokenValiditySeconds());
            }
            if (client.getRefreshTokenValiditySeconds() != null) {
                clientBuilder.refreshTokenValiditySeconds(
                        client.getRefreshTokenValiditySeconds());
            }
            if (client.getRegisteredRedirectUri() != null) {
                clientBuilder.redirectUris(
                        client.getRegisteredRedirectUri().toArray(new String[0]));
            }
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(this.authenticationManager);
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
