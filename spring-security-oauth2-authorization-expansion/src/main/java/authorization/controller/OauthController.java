package authorization.controller;

import authorization.config.SecurityProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-14 下午3:21
 */
@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final @NonNull SecurityProperties securityProperties;

    @GetMapping("login")
    public String loginView(Model model) {
        model.addAttribute("action", securityProperties.getLoginProcessingUrl());
        return "form-login";
    }

}
