package authorization.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-14 下午3:26
 */
@Controller
@SessionAttributes("authorizationRequest")
public class AuthorizationController {
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("authorization");
        view.addObject("clientId", authorizationRequest.getClientId());
        // 传递 scope 过去,Set 集合
        view.addObject("scopes", authorizationRequest.getScope());
        // 拼接一下名字
        view.addObject("scopeName", String.join(",", authorizationRequest.getScope()));
        return view;
    }
}
