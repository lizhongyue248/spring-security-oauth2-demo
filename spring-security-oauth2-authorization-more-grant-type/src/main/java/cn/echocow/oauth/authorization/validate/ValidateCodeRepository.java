package cn.echocow.oauth.authorization.validate;


import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码资源处理
 *
 * @author echo
 * @date 2019/7/28 下午10:44
 */
public interface ValidateCodeRepository {

    /**
     * 保存
     *
     * @param request 请求
     * @param code    验证码
     * @param type    类型
     */
    void save(ServletWebRequest request, String code, String type);

    /**
     * 获取
     *
     * @param request 请求
     * @param type    类型
     * @return 验证码
     */
    String get(ServletWebRequest request, String type);

    /**
     * 移除
     *
     * @param request 请求
     * @param type    类型
     */
    void remove(ServletWebRequest request, String type);


}
