package cn.echocow.oauth.authorization.config;

import cn.echocow.oauth.authorization.auth.sms.SmsAuthenticationFilter;
import cn.echocow.oauth.authorization.auth.sms.SmsAuthenticationProvider;
import cn.echocow.oauth.authorization.auth.sms.SmsAuthenticationSecurityConfig;
import cn.echocow.oauth.authorization.validate.ValidateCodeFilter;
import cn.echocow.oauth.authorization.validate.ValidateCodeGranterFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * spring security
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-27 下午9:54
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final @NonNull ValidateCodeFilter validateCodeFilter;
    private final @NonNull ValidateCodeGranterFilter validateCodeGranterFilter;
    private final @NonNull SmsAuthenticationSecurityConfig smsAuthenticationSecurityConfig;

    /**
     * 密码加密方式，spring 5 后必须对密码进行加密
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 创建两个内存用户
     * 用户名 user 密码 123456 角色 ROLE_USER
     * 用户名 admin 密码 admin 角色 ROLE_ADMIN
     *
     * @return InMemoryUserDetailsManager
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("123456"))
                .authorities("ROLE_USER").build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities("ROLE_ADMIN").build());
        manager.createUser(User.withUsername("13712341234")
                .password(passwordEncoder().encode("123456"))
                .authorities("ROLE_ADMIN").build());
        return manager;
    }

    /**
     * 认证管理
     *
     * @return 认证管理对象
     * @throws Exception 认证异常信息
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .apply(smsAuthenticationSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers("/code/*").permitAll()
                .antMatchers("/auth/sms").permitAll()
                .antMatchers("/custom/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .and()
                .httpBasic();


        http
                .addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterBefore(validateCodeGranterFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
