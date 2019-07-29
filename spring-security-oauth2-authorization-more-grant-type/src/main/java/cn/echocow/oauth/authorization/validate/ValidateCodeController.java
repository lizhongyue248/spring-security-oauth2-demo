package cn.echocow.oauth.authorization.validate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 动态获取验证码
 * 决策器，用来决定使用哪一个抽象策略的，同时接收用户请求。
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午10:57
 */
@RestController
@RequiredArgsConstructor
public class ValidateCodeController {


    private final @NonNull ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 通过 type 进行查询到对应的处理器
     * 同时创建验证码
     *
     * @param request  请求
     * @param response 响应
     * @param type     验证码类型
     * @throws Exception 异常
     */
    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable String type) throws Exception {
        validateCodeProcessorHolder.findValidateCodeProcessor(type)
                .create(new ServletWebRequest(request, response));
    }

}
