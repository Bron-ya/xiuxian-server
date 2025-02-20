package com.xx.xiuxianserver.Security.Login.Sms;

import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 自定义的 Authentication 认证对象
 * 必须是 {@link org.springframework.security.core.Authentication} 实现类
 * 这里选择 extends{@link org.springframework.security.authentication.AbstractAuthenticationToken}，而不是直接 implements Authentication
 * 是因为可以减少代码编写，其中 {@link org.springframework.security.core.Authentication} 定义了很多接口，我们用不上
 * @author jiangzk
 * 2025/1/24  下午4:31
 */
@Getter
@Setter
public class SmsAuthentication  extends AbstractAuthenticationToken {

    // 前端传过来的 手机号和验证码
    private String phone;
    private String code;
    // 存 token 信息的类
    private GlobalJWTPayload globalJwtPayload;

    public SmsAuthentication() {
        super(null);
    }

    /**
     * 获取用户凭证信息
     * 根据SpringSecurity的设计：授权成功后，Credential凭证（比如登录密码，验证码）信息需要被清空
     */
    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : code;
    }

    /**
     * 获取用户信息
     * 根据SpringSecurity的设计：授权成功之后，返回当前登陆用户的信息；授权成功之前，返回客户端传过来的数据（比如用户名，手机号）
     */
    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? globalJwtPayload : phone;
    }
}
