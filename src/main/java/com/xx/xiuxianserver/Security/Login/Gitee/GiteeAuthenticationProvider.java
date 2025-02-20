package com.xx.xiuxianserver.Security.Login.Gitee;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Utils.MyJsonUtils;
import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import com.xx.xiuxianserver.Entity.User;
import com.xx.xiuxianserver.Mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录认证处理器（返回认证成功的用户信息）
 * @author jiangzk
 * 2025/1/23  下午5:25
 */
@Component
public class GiteeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserMapper userMapper;

    @Resource
    private GiteeApiClient giteeApiClient;

    public GiteeAuthenticationProvider(){
        super();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 1.获取 code 信息
        String code = (String) authentication.getCredentials();
        try {
            // 1.1 通过 code 获取 token，并校验是否合理
            String token = giteeApiClient.getTokenByCode(code);
            if (token == null) {
                throw new MyException(HttpStatus.BAD_REQUEST.value(), "Gitee授权失败!");
            }
            // 1.2 通过传入的 token 值获取当前用户的 openId
            Map<String, Object> thirdUser = giteeApiClient.getThirdUserInfo(token);
            if (thirdUser == null) {
                throw new MyException(AppHttpCodeEnum.USER_NOT_FOUND.getCode(),
                        AppHttpCodeEnum.USER_NOT_FOUND.getMsg());
            }
            String openId = thirdUser.get("openId").toString();

            // todo 若需要接入 gitee 或其他第三方应用，根据需求进行添加 user 字段信息
            User user = new User();
            boolean notBindAccount = true;
            // 2.通过第三方的账号唯一id，去匹配数据库中已有的账号信息
//            user = userMapper.getUserByOpenId(openId);
//            notBindAccount = user != null; // gitee账号没有绑定我们系统的用户
//            if (notBindAccount) {
//                // 没找到账号信息，那就是第一次使用gitee登录，可能需要创建一个新用户
//                userMapper.createUserWithOpenId(user, openId);
//            }

            // 3.当前登录用户认证通过，封装用户信息到 token 进行返回
            GiteeAuthentication giteeAuthentication = new GiteeAuthentication();
            giteeAuthentication.setAuthenticated(true);
            giteeAuthentication.setGlobalJwtPayload(MyJsonUtils.convert(user, GlobalJWTPayload.class));
            // 3.1 传入第三方应用需要的特殊参数
            HashMap<String, Object> loginDetail = new HashMap<>();
            // 第一次使用三方账号登录，需要告知前端，让前端跳转到初始化账号页面（可能需要）
            loginDetail.put("needInitUserInfo", notBindAccount);
            loginDetail.put("nickname", thirdUser.get("nickname").toString());
            giteeAuthentication.setDetails(loginDetail);
            return giteeAuthentication;
        } catch (MyException e) {
            // 转换已知异常，将异常内容返回给前端
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            // 未知异常
            throw new BadCredentialsException("Gitee Authentication Failed");
        }
    }

    /**
     * 【注意】
     * 只有当传入的认证请求类型是 GiteeAuthentication 类型或其子类时，当前的认证提供者才会尝试处理这个认证请求
     * 这通常意味着你有一个专门用来处理用户名和密码认证的认证提供者，它只对 GiteeAuthentication 类型的认证请求感兴趣
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(GiteeAuthentication.class);
    }
}
