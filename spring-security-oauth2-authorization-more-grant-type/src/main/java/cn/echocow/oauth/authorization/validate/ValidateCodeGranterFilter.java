package cn.echocow.oauth.authorization.validate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 验证码过滤器。
 *
 * <p>继承于 {@link OncePerRequestFilter} 确保在一次请求只通过一次filter</p>
 * <p>需要配置指定拦截路径，默认拦截 POST 请求</p>
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午11:15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeGranterFilter extends OncePerRequestFilter {

    private final @NonNull ValidateCodeProcessorHolder validateCodeProcessorHolder;
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/oauth/token", HttpMethod.POST.name());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)){
            String grantType = getGrantType(request);
            if ("sms".equalsIgnoreCase(grantType) || "email".equalsIgnoreCase(grantType)){
                try {
                    log.info("请求需要验证！验证请求：" + request.getRequestURI() + " 验证类型：" + grantType);
                    validateCodeProcessorHolder.findValidateCodeProcessor(grantType)
                            .validate(new ServletWebRequest(request, response));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getGrantType(HttpServletRequest request) {
        return request.getParameter("grant_type");
    }

}
