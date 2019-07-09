package cn.echocow.oauth.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 资源测试
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-9 下午2:50
 */
@RestController
public class ResourceController {

    /**
     * 获取当前登录用户的信息
     *
     * @param principal 当前的登录用户
     * @return 响应
     */
    @GetMapping("/resource")
    public HttpEntity<?> resource(Principal principal) {
        return ResponseEntity.ok(principal);
    }

}
