package cn.echocow.oauth.authorization.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * oauth2 控制器
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 2019/7/28 下午11:37
 */
@RestController
@RequestMapping("/auth")
public class SmsValidateCodeController {

    @PostMapping("/sms")
    public HttpEntity<?> sms() {
        return ResponseEntity.ok("ok");
    }
}
