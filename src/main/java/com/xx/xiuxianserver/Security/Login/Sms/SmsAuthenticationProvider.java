package com.xx.xiuxianserver.Security.Login.Sms;


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
import org.springframework.stereotype.Component;

/**
 * 用户登录认证处理器（返回认证成功的用户信息）
 * @author jiangzk
 * 2025/1/24  下午4:49
 */
@Component
public class SmsAuthenticationProvider  implements AuthenticationProvider {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    public SmsAuthenticationProvider(){
        super();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 1.获取用户提交的用户名和密码：
        String phone = (String)authentication.getPrincipal();
        String code = (String) authentication.getCredentials();

        // 2.检验当前验证码是否正确
        if (validateSmsCode(code)) {
            // 2.1 若验证码正确，查询用户信息，进行匹配
            User user = userMapper.selectByPhone(phone);
            if (user == null) {
                // 这里需要抛指定异常，AuthenticationFailureHandler的认证失败响应处理器会处理这个异常
                throw new BadCredentialsException(AppHttpCodeEnum.USER_NOT_FOUND.getMsg());
            }
            if (!user.getStatus()) {
                throw new MyException(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "当前用户是未启用状态，无法进行登录操作");
            }

            // 3.当前登录用户认证通过，封装用户信息到 token 进行返回
            SmsAuthentication smsAuthentication = new SmsAuthentication();
            // 设为 true，告诉 security 当前用户已经认证成功
            smsAuthentication.setAuthenticated(true);
            // 给 JWTPayload 赋值，方便之后从 Authentication 对象中获取当前认证成功的用户信息
            GlobalJWTPayload globalJwtPayload = new GlobalJWTPayload();
            int roleId = userRoleMapper.selectRoleIdByUserId(user.getId());
            globalJwtPayload.setRoleId(roleId);
            globalJwtPayload.setUserId(user.getId());
            smsAuthentication.setGlobalJwtPayload(globalJwtPayload);
            return smsAuthentication;
        } else {
            // 这里需要抛指定异常，AuthenticationFailureHandler的认证失败响应处理器会处理这个异常
            throw new BadCredentialsException(AppHttpCodeEnum.PHONE_OR_CODE_ERROR.getMsg());
        }
    }

    /**
     * 【解释】
     * 只有当传入的认证请求类型是 SmsAuthentication 类型或其子类时，当前的认证提供者才会尝试处理这个认证请求
     * 这通常意味着你有一个专门用来处理用户名和密码认证的认证提供者，它只对 SmsAuthentication 类型的认证请求感兴趣
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(SmsAuthentication.class);
    }

    /**
     * 验证当前验证码是否正确
     */
    private boolean validateSmsCode(String smsCode) {
        // todo 后期根据需求进行编写传入验证码功能
        return "123456".equals(smsCode);
    }
}