package cn.echocow.oauth.authorization.auth.sms;

import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 授权提供者
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/29 下午10:57
 */
@Setter
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // 获取用户信息
        UserDetails user = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal().toString());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无效认证");
        }
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 类型进行匹配
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
