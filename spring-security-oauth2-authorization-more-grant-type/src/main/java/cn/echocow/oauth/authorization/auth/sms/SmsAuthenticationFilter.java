package cn.echocow.oauth.authorization.auth.sms;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登录授权过滤器
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/29 下午10:50
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    SmsAuthenticationFilter() {
        // 需要拦截的路径
        super(new AntPathRequestMatcher("/oauth/sms", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        // 获取参数
        String sms = obtainSms(request);
        sms = sms == null ? "" : sms.trim();
        // 需要创建我们自己的授权 token
        SmsAuthenticationToken authRequest = new SmsAuthenticationToken(sms);
        setDetails(request, authRequest);
        // 授权管理器对请求进行授权
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取请求中的 sms 值
     *
     * @param request 正在为其创建身份验证请求
     * @return 请求中的 sms 值
     */
    private String obtainSms(HttpServletRequest request) {
        return request.getParameter("sms");
    }

    /**
     * 提供以便子类可以配置放入 authentication request 的 details 属性的内容
     *
     * @param request     正在为其创建身份验证请求
     * @param authRequest 应设置其详细信息的身份验证请求对象
     */
    private void setDetails(HttpServletRequest request,
                            SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
