package com.xx.xiuxianserver.Security.Login.UserName;

import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义的 Authentication 认证对象
 * 必须是 {@link org.springframework.security.core.Authentication} 实现类
 * 这里选择 extends{@link org.springframework.security.authentication.AbstractAuthenticationToken}，而不是直接 implements Authentication
 * 是因为可以减少代码编写，其中 {@link org.springframework.security.core.Authentication} 定义了很多接口，我们用不上
 * @author jiangzk
 * 2025/1/23  下午4:03
 */
@Getter
@Setter
public class UserNameAuthentication extends AbstractAuthenticationToken {

    // 前端传过来的 用户名和密码
    private String username;
    private String password;
    // 存 token 信息的类
    private GlobalJWTPayload globalJwtPayload;

    public UserNameAuthentication() {
        super(null);
    }

    public UserNameAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(false); // 初始状态未认证
    }

    /**
     * 获取用户凭证信息
     * 授权成功后，Credential凭证（比如登录密码，验证码）信息需要被清空
     */
    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : password;
    }

    /**
     * 获取用户信息
     * 授权成功之后，返回当前登陆用户的信息；
     * 授权成功之前，返回客户端传过来的数据（比如用户名，手机号）
     */
    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? globalJwtPayload : username;
    }
}