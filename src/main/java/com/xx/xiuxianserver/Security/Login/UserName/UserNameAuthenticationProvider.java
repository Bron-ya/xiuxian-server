package com.xx.xiuxianserver.Security.Login.UserName;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import com.xx.xiuxianserver.Entity.User;
import com.xx.xiuxianserver.Mapper.UserMapper;
import com.xx.xiuxianserver.Mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用户登录认证处理器（返回认证成功的用户信息）
 * @author jiangzk
 * 2025/1/23  下午5:25
 */
@Component
public class UserNameAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    public UserNameAuthenticationProvider(){
        super();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 1.获取用户提交的用户名和密码：
        String username = (String)authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // 2.查询用户信息，进行匹配
        User user = userMapper.selectByUserName(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            // 这里需要抛指定异常，AuthenticationFailureHandler的认证失败响应处理器会处理这个异常
            throw new BadCredentialsException(AppHttpCodeEnum.LOGIN_ERROR.getMsg());
        }
        if (!user.getStatus()){
            throw new MyException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "当前用户是未启用状态，无法进行登录操作");
        }

        // 3.当前登录用户认证通过，封装用户信息到 token 进行返回
        UserNameAuthentication userNameAuthentication = new UserNameAuthentication();
        // 设为 true，告诉 security 当前用户已经认证成功
        userNameAuthentication.setAuthenticated(true);
        // 传入当前认证用户的权限信息
        // 给 JWTPayload 赋值，方便之后从 Authentication 对象中获取当前认证成功的用户信息
        GlobalJWTPayload globalJwtPayload = new GlobalJWTPayload();
        int roleId = userRoleMapper.selectRoleIdByUserId(user.getId());
        globalJwtPayload.setRoleId(roleId);
        globalJwtPayload.setUserId(user.getId());
        userNameAuthentication.setGlobalJwtPayload(globalJwtPayload);
        return userNameAuthentication;
    }

    /**
     * 【解释】
     * 只有当传入的认证请求类型是 UserNameAuthentication 类型或其子类时，当前的认证提供者才会尝试处理这个认证请求
     * 这通常意味着你有一个专门用来处理用户名和密码认证的认证提供者，它只对 UserNameAuthentication 类型的认证请求感兴趣
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserNameAuthentication.class);
    }
}
